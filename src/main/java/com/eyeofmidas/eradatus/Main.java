package com.eyeofmidas.eradatus;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.ServletHandler;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;

import com.eyeofmidas.eradatus.security.AuthFilter;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class Main {

	private static boolean secure = true;
	public static final URI BASE_URI = getBaseURI();
	public static final String CONTENT = "Eradatus Server";

	// set to true if using ssl certificates for security

	private static int getPort(int defaultPort) {
		// grab port from environment, otherwise fall back to default port 9998
		String httpPort = System.getProperty("jersey.test.port");
		if (null != httpPort) {
			try {
				return Integer.parseInt(httpPort);
			} catch (NumberFormatException e) {
			}
		}
		return defaultPort;
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http" + (secure ? "s" : "") + "://localhost/").port(getPort(9998)).build();
	}

	protected static HttpServer startServer() throws IOException {
		SSLContextConfigurator sslContext = new SSLContextConfigurator();
		ServletHandler handler = new ServletHandler();
		handler.addInitParameter(PackagesResourceConfig.PROPERTY_PACKAGES, "com.eyeofmidas.eradatus");

		if (secure) {
			sslContext.setKeyStoreFile("./server.jks");
			sslContext.setKeyStorePass("eradatus");
			sslContext.setTrustStoreFile("./server.tst");
			sslContext.setTrustStorePass("eradatus");
			handler.addInitParameter(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, AuthFilter.class.getName());
		}
		handler.setServletInstance(new ServletContainer());

		SSLEngineConfigurator sslEngineConfigurator = new SSLEngineConfigurator(sslContext);
		sslEngineConfigurator.setClientMode(false);
		if (secure) {
			sslEngineConfigurator.setWantClientAuth(true);
		}

		System.out.println("Starting grizzly2...");
		return GrizzlyServerFactory.createHttpServer(BASE_URI, handler, secure, sslEngineConfigurator);
	}

	public static void main(String[] args) throws IOException {
		HttpServer httpServer = startServer();
		System.out.println(String.format("Jersey app started with WADL available at " + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
		System.in.read();
		httpServer.stop();
	}
}
