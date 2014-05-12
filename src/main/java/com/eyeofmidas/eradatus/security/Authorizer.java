package com.eyeofmidas.eradatus.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.eyeofmidas.eradatus.security.User;

public class Authorizer implements SecurityContext {

	private User user;
	private Principal principal;
	private UriInfo uriInfo;

	public Authorizer(final User user, UriInfo uriInfo) {
		this.user = user;
		this.uriInfo = uriInfo;
		this.principal = new Principal() {

			public String getName() {
				return user.username;
			}
		};
	}

	public Principal getUserPrincipal() {
		return this.principal;
	}

	public boolean isUserInRole(String role) {
		return (role.equals(user.role));
	}

	public boolean isSecure() {
		return "https".equals(uriInfo.getRequestUri().getScheme());
	}

	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}
}
