package de.uniluebeck.compilatex.service;

import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
@Path("/latex")
public class LatexEnvironmentService {

    private final RequestHandler requestHandler = new RequestHandler();

    @GET
    @Path("environments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLatexEnvironments() {
        Logger.getLogger(LatexEnvironmentService.class.getName()).info("get latex environments service called");
        return requestHandler.handleGetLatexEnvironments();
    }
}
