package de.uniluebeck.compilatex.service;

import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
@Path("/jobs")
public class JobService {

    private final RequestHandler requestHandler = new RequestHandler();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createJob() {
        return requestHandler.handleCreateJob();
    }

    @GET
    @Path("{jobId}/compile/{latexEnvironment}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response compile(@PathParam("jobId") final Long jobId, @PathParam("latexEnvironment") final String latexEnvironment) { //, @Suspended final AsyncResponse asyncResponse) {
        Logger.getLogger(JobService.class.getName()).info("compile job service called");
//        Logger.getLogger(JobService.class.getName()).info("latexEnvironment: " + latexEnvironment);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Response response = requestHandler.handleCompile(jobId, latexEnvironment);
//                asyncResponse.resume(response);
//            }
//        }).start();
//        return Response.ok().build();
        return requestHandler.handleCompile(jobId, latexEnvironment);
    }

    @GET
    @Path("{jobId}/log")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getLog(@PathParam("jobId") final Long jobId) {
        Logger.getLogger(JobService.class.getName()).info("compile job service called");
        return requestHandler.handleGetLog(jobId);
    }

    @GET
    @Path("{jobId}/log/html")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getHtmlLog(@PathParam("jobId") final Long jobId) {
        Logger.getLogger(JobService.class.getName()).info("compile job service called");
        return requestHandler.handleGetHtmlLog(jobId);
    }

    @GET
    @Path("{jobId}/pdf")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getPdf(@PathParam("jobId") final Long jobId) {
        Logger.getLogger(JobService.class.getName()).info("compile job service called");
        return requestHandler.handleGetPdf(jobId);
    }

    @DELETE
    @Path("{jobId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteJob(@PathParam("jobId") final Long jobId) {
        Logger.getLogger(JobService.class.getName()).info("compile job service called");
        return requestHandler.handleDeleteJob(jobId);
    }
}
