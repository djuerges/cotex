package de.uniluebeck.collaboratex.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
public class ProjectFileService {

    private final RequestHandler requestHandler = new RequestHandler();

    @GET
    @Path("/uploadurl")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getFileUploadUrl() {
        return requestHandler.handleGetFileUploadUrl();
    }
    
    @POST
    @Path("{projectId}/files")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createFile(@PathParam("projectId") final Long projectId, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        Logger.getLogger(ProjectFileService.class.getName()).log(Level.WARNING, "create file project file service called");
        return requestHandler.handleCreateFile(projectId, request, response);
    }
    
    @POST
    @Path("{projectId}/files/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importFiles(@PathParam("projectId") final Long projectId, @Context HttpServletRequest request) {
        Logger.getLogger(ProjectFileService.class.getName()).log(Level.WARNING, "import files project file service called");
        return requestHandler.handleImportFiles(projectId, request);
    }
    
    @GET
    @Path("{projectId}/files/{fileId}/meta")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFileMetaData(@PathParam("projectId") final Long projectId, @PathParam("fileId") final Long fileId) {
        Logger.getLogger(ProjectFileService.class.getName()).log(Level.WARNING, "get file meta data project file service called");
        return requestHandler.handleGetFileMetaData(projectId, fileId);
    }
    
    @GET
    @Path("{projectId}/files/{fileId}")
    public Response getFileContent(@PathParam("projectId") final Long projectId, @PathParam("fileId") final Long fileId, @Context HttpServletResponse response) {
        Logger.getLogger(ProjectFileService.class.getName()).log(Level.WARNING, "get file content project file service called");
        return requestHandler.handleGetFileContent(projectId, fileId, response);
    }
    
    @POST
    @Path("{projectId}/files/{fileId}/setmaintex")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setMainTexFile(@PathParam("projectId") final Long projectId, @PathParam("fileId") final Long fileId, @FormParam("mainTex") final boolean mainTex) {
        Logger.getLogger(ProjectFileService.class.getName()).log(Level.WARNING, "set as main tex file project file service called");
        return requestHandler.handleSetMainTexFile(projectId, fileId, mainTex);
    }
    
    @POST
    @Path("{projectId}/files/{fileId}/rename")
    @Produces(MediaType.APPLICATION_JSON)
    public Response renameFile(@PathParam("projectId") final Long projectId, @PathParam("fileId") final Long fileId, @FormParam("value") String name) {
        Logger.getLogger(ProjectFileService.class.getName()).log(Level.WARNING, "rename file project file service called {0}", name.trim());
        return requestHandler.handleRenameFile(projectId, fileId, name.trim());
    }
    
    @POST
    @Path("{projectId}/files/{fileId}/patch")
    @Produces(MediaType.APPLICATION_JSON)
    public Response patchFileContent(@PathParam("projectId") final Long projectId, @PathParam("fileId") final Long fileId, @FormParam("patch") String patch, @Context HttpServletRequest request) {
        Logger.getLogger(ProjectFileService.class.getName()).log(Level.WARNING, "patch file content service called");
        return requestHandler.handlePatchFileContent(projectId, fileId, patch, request);
    }
    
    @POST
    @Path("{projectId}/files/{fileId}/content")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFileContent(@PathParam("projectId") final Long projectId, @PathParam("fileId") final Long fileId, @FormParam("content") String content) {
        Logger.getLogger(ProjectFileService.class.getName()).log(Level.WARNING, "update file content service called");
        return requestHandler.handleUpdateFileContent(projectId, fileId, content);
    }
    
    @PUT
    @Path("{projectId}/files/{fileId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFile(@PathParam("projectId") final Long projectId, @PathParam("fileId") final Long fileId,  @Context HttpServletRequest request, @Context HttpServletResponse response) {
        Logger.getLogger(ProjectFileService.class.getName()).log(Level.WARNING, "update file project file service called");
        return requestHandler.handleUpdateFile(projectId, fileId, request, response);
    }

    @DELETE
    @Path("{projectId}/files/{fileId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFile(@PathParam("projectId") final Long projectId, @PathParam("fileId") final Long fileId) {
        Logger.getLogger(ProjectFileService.class.getName()).log(Level.WARNING, "delete file project file service called");
        return requestHandler.handleDeleteFile(projectId, fileId);
    }
}
