package com.eyeofmidas.eradatus.security;

public class AuthenticationException extends RuntimeException {

	public AuthenticationException(String message, String realm) {
		super(message);
		this.realm = realm;
	}

	private String realm = null;

	public String getRealm() {
		return this.realm;
	}

}