package com.eyeofmidas.eradatus.security;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import com.sun.jersey.api.container.MappableContainerException;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class SecurityFilter implements ContainerRequestFilter {

	UriInfo uriInfo;
	private static final String REALM = "Eradatus HTTPS authorization";

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		User user = authenticate(request);
		request.setSecurityContext(new Authorizer(user, uriInfo));
		return request;
	}

	private User authenticate(ContainerRequest request) {
		// Extract authentication credentials
		String authentication = request.getHeaderValue(ContainerRequest.AUTHORIZATION);
		if (authentication == null) {
			throw new MappableContainerException(new AuthenticationException("Authentication credentials are required", REALM));
		}
		if (!authentication.startsWith("Basic ")) {
			return null;
			// additional checks should be done here
			// "Only HTTP Basic authentication is supported"
		}
		authentication = authentication.substring("Basic ".length());
		String[] values = new String(Base64.base64Decode(authentication)).split(":");
		if (values.length < 2) {
			throw new WebApplicationException(400);
			// "Invalid syntax for username and password"
		}
		String username = values[0];
		String password = values[1];
		if ((username == null) || (password == null)) {
			throw new WebApplicationException(400);
			// "Missing username or password"
		}

		// Validate the extracted credentials
		User user = null;

		if (username.equals("user") && password.equals("password")) {
			user = new User("user", "user");
			System.out.println("USER AUTHENTICATED");
			// } else if (username.equals("admin") &&
			// password.equals("adminadmin")) {
			// user = new User("admin", "admin");
			// System.out.println("ADMIN AUTHENTICATED");
		} else {
			System.out.println("USER NOT AUTHENTICATED");
			throw new MappableContainerException(new AuthenticationException("Invalid username or password\r\n", REALM));
		}
		return user;
	}

}
