/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniluebeck.collaboratex.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
@Path("/session")
public class SessionService {
    
    private final RequestHandler requestHandler = new RequestHandler();

    @GET
    @Path("/channel/token")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getToken(@Context HttpServletRequest request) {
//        Logger.getLogger(SessionService.class.getName()).log(Level.WARNING, "get token service called with new session?");
        return requestHandler.handleGetToken(request);
    }
    
    @POST
    @Path("/document/{fileId}/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerForDocument(@PathParam("fileId") final Long fileId, @Context HttpServletRequest request) {
//        Logger.getLogger(ProjectFileService.class.getName()).log(Level.WARNING, "register for document " + fileId + " service called by " + request.getSession().getId() + " @ " + request.getHeader("User-Agent"));
        return requestHandler.handleRegisterForDocument(fileId, request);
    }
    
    @POST
    @Path("/document/{fileId}/unregister")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unregisterForDocument(@PathParam("fileId") final Long fileId, @Context HttpServletRequest request) {
//        Logger.getLogger(ProjectFileService.class.getName()).log(Level.WARNING, "unregister for document " + fileId + " service called " + request.getSession().getId() + " @ " + request.getHeader("User-Agent"));
        return requestHandler.handleUnregisterForDocument(fileId, request);
    }
}