package com.eyeofmidas.eradatus.security;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.api.container.MappableContainerException;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class AuthFilter implements ContainerRequestFilter {
	private static final String REALM = "Eradatus HTTPS Authentication";

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		String method = request.getMethod();
		String path = request.getPath(true);

		// We do allow wadl to be retrieved
		if (method.equals("GET") && path.equals("application.wadl")) {
			return request;
		}

		User user = authenticate(request);
		request.setSecurityContext(new Authorizer(user));
		return request;

	}

	private User authenticate(ContainerRequest request) throws WebApplicationException {
		String auth = request.getHeaderValue(ContainerRequest.AUTHORIZATION);
		if (auth == null) {
			throw new MappableContainerException(new AuthenticationException("Authentication credentials are required", REALM));
		}

		if (!auth.startsWith("Basic ")) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		String[] loginPassword = BasicAuth.decode(auth);

		// If login or password fail
		if (loginPassword == null || loginPassword.length != 2) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		User user = null;
		// DO YOUR DATABASE CHECK HERE (replace that line behind)...
		if (loginPassword[0].equals("user") && loginPassword[1].equals("password")) {
			user = new User("user", "user");
		}

		// Our system refuse login and password
		if (user == null) {
			throw new MappableContainerException(new AuthenticationException("Invalid username or password\r\n", REALM));
		}

		return user;
	}
}
