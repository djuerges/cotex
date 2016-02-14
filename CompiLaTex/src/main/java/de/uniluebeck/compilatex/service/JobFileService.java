package de.uniluebeck.compilatex.service;

import java.io.InputStream;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
@Path("/jobs")
public class JobFileService {

    private final RequestHandler requestHandler = new RequestHandler();

    @POST
    @Path("{jobId}/files")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addFile(@PathParam("jobId") final Long jobId, @FormDataParam("file") InputStream fileInputStream, @FormDataParam("filename") final String filename, @FormDataParam("isMainTex") final boolean isMainTex) {
        Logger.getLogger(JobFileService.class.getName()).info("add job file service called");
        return requestHandler.handleAddFile(jobId, isMainTex, filename, fileInputStream);
    }
    
    @PUT
    @Path("{jobId}/files/{fileId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFile(@PathParam("jobId") final Long jobId, @PathParam("fileId") final Long fileId, @FormDataParam("file") InputStream fileInputStream, @FormDataParam("filename") final String filename) {
        Logger.getLogger(JobFileService.class.getName()).info("update job file service called");
        return requestHandler.handleUpdateFile(jobId, fileId, filename, fileInputStream);
    }

    @GET
    @Path("{jobId}/files/{fileId}/lastchanged")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getLastFileModificationDate(@PathParam("jobId") final Long jobId, @PathParam("fileId") final Long fileId) {
        Logger.getLogger(JobFileService.class.getName()).info("get last modification file service called");
        return requestHandler.handleGetLastFileModificationDate(jobId, fileId);
    }

    @DELETE
    @Path("{jobId}/files/{fileId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFile(@PathParam("jobId") final Long jobId, @PathParam("fileId") final Long fileId) {
        Logger.getLogger(JobFileService.class.getName()).info("delete job file service called");
        return requestHandler.handleDeleteFile(jobId, fileId);
    }
}
