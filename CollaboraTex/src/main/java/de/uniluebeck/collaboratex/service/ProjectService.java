package de.uniluebeck.collaboratex.service;

import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
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
@Path("/projects")
public class ProjectService {

    private final RequestHandler requestHandler = new RequestHandler();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProject() {
        Logger.getLogger(ProjectService.class.getName()).warning("create project service called");
        return requestHandler.handleCreateProject();
    }
    
    @POST
    @Path("/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importProject(@Context HttpServletRequest request) {
        return requestHandler.handleImportProject(request);
    }
    
    @GET
    @Path("{projectId}/download")
    public Response downloadProject(@PathParam("projectId") final Long projectId) {
        return requestHandler.handleDownloadProject(projectId);
    }
    
    @POST
    @Path("{projectId}/rename")
    @Produces(MediaType.APPLICATION_JSON)
    public Response renameProject(@PathParam("projectId") final Long projectId, @FormParam("value") String name) {
        return requestHandler.handleRenameProject(projectId, name);
    }

    @DELETE
    @Path("{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProject(@PathParam("projectId") final Long projectId) {
        return requestHandler.handleDeleteProject(projectId);
    }
}
