package com.eyeofmidas.eradatus.security;

import javax.xml.bind.DatatypeConverter;

public class BasicAuth {
	public static String[] decode(String auth) {
		auth = auth.replaceFirst("[B|b]asic ", "");

		// Decode the Base64 into byte[]
		byte[] decodedBytes = DatatypeConverter.parseBase64Binary(auth);

		// If the decode fails in any case
		if (decodedBytes == null || decodedBytes.length == 0) {
			return null;
		}

		// Now we can convert the byte[] into a split array:
		// - the first one is login,
		// - the second one password
		return new String(decodedBytes).split(":", 2);
	}
}