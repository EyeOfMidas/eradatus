package com.eyeofmidas.eradatus.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.eyeofmidas.eradatus.Main;

@Path("/")
public class RootResource {

	@GET
	public Response get(@Context HttpHeaders headers) {
		return Response.ok(Main.CONTENT).type(MediaType.APPLICATION_JSON).build();
	}
}
