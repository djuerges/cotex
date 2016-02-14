package de.uniluebeck.collaboratex.service;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreFailureException;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import de.uniluebeck.collaboratex.algorithm.diff_match_patch;
import de.uniluebeck.collaboratex.algorithm.diff_match_patch.Patch;
import de.uniluebeck.collaboratex.dao.ProjectDAO;
import de.uniluebeck.collaboratex.dto.ProjectDTO;
import de.uniluebeck.collaboratex.dto.ProjectFileDTO;
import de.uniluebeck.collaboratex.entity.Project;
import de.uniluebeck.collaboratex.search.SearchIndexManager;
import de.uniluebeck.collaboratex.session.SessionManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.eclipse.persistence.sessions.serializers.JSONSerializer;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class RequestHandler extends JSONSerializer {

    private final BlobInfoFactory infoFactory = new BlobInfoFactory();
    private final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    private final ChannelService channelService = ChannelServiceFactory.getChannelService();

    /**
     * create new project
     *
     * @return Response containing the created resource and indicating whether
     * the service call was successful or errors occured during execution
     */
    public Response handleCreateProject() {
        /* instantiate new DAO */
        ProjectDAO dao = new ProjectDAO();

        /* create new project and save */
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("New Project");
        projectDTO = dao.save(projectDTO);

        return Response.status(Status.CREATED).entity(projectDTO).build();
    }

    /**
     * the Google App Engine Blobstore requires a unique upload url for every
     * file that is added to it, so generate and return one
     *
     * !!! path relates to the importProject service in ProjectService
     *
     * @@return Response indicating whether the service call was successful or
     * errors occured during execution
     */
    public Response handleGetFileUploadUrl() {
        /* url must point to the path where files will be POSTed to -> importProject in ProjectService */
        String url = BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/rest/projects/import");
        return Response.ok().entity(url).build();
    }

    /**
     * import project
     *
     * @param request HttpServletRequest containing form data and files
     * @return Response containing the created resource and indicating whether
     * the service call was successful or errors occured during execution
     */
    public Response handleImportProject(HttpServletRequest request) {

        /* instantiate new DAO */
        ProjectDAO dao = new ProjectDAO();

        /* create new project and set name */
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName(request.getParameter("projectname"));

        /* store blobs and set files in project */
        Set<ProjectFileDTO> files = storeBlobs(request);
        projectDTO.setFiles(files);

        /* persist new project */
        projectDTO = dao.save(projectDTO);

        /* make all files searchable by adding them to the search index */
        SearchIndexManager.INSTANCE.addToIndex(projectDTO.getFiles());

        return Response.status(Status.CREATED).entity(projectDTO).build();
    }

    /**
     * download project
     *
     * @param projectId id for the project
     * @return Response containing the created resource and indicating whether
     * the service call was successful or errors occured during execution
     */
    public Response handleDownloadProject(Long projectId) {

        /* instantiate new DAO */
        ProjectDAO dao = new ProjectDAO();

        /* find project in db */
        ProjectDTO projectDTO = dao.findById(projectId);

        /* log and return error code if project was not found */
        if (projectDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        } else {
            /* create output streams */
            ByteArrayOutputStream zipBaos = new ByteArrayOutputStream();
            try (ZipOutputStream zipOut = new ZipOutputStream(zipBaos)) {

                for (ProjectFileDTO fileDTO : projectDTO.getFiles()) {

                    /* get blob infos and contents as byte array */
                    BlobInfo info = infoFactory.loadBlobInfo(fileDTO.getBlobKey());
                    byte[] blobContents = blobstoreService.fetchData(fileDTO.getBlobKey(), 0, info.getSize());

                    /* add blob to zip file */
                    zipOut.putNextEntry(new ZipEntry(info.getFilename()));
                    zipOut.write(blobContents);
                    zipOut.closeEntry();
                }
                /* close output stream */
                zipOut.close();

                /* get zip data from ByteArrayOutputStream and close stream */
                byte[] zipData = zipBaos.toByteArray();
                zipBaos.close();

                /* return created zip file */
                return Response.ok().type("application/zip").header("Content-Disposition", "attachment; filename=\"" + projectDTO.getName() + ".zip\"").entity(zipData).build();
            } catch (IOException ex) {
                Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "couldn't create zip file", ex);
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    /**
     * rename project
     *
     * @param projectId id for the project
     * @param name the new name of the project
     * @return Response containing whether the rename was successful
     */
    public Response handleRenameProject(Long projectId, String name) {
        /* instantiate new DAO */
        ProjectDAO dao = new ProjectDAO();

        /* get project and set new name or log and return error code if project was not found */
        ProjectDTO projectDTO = dao.findById(projectId);
        if (projectDTO == null || name.isEmpty()) {
            return Response.status(Status.BAD_REQUEST).build();
        } else {
            projectDTO.setName(name);
            return Response.status(Status.OK).entity(projectDTO).build();
        }
    }

    /**
     * delete project with given id
     *
     * @param projectId id for the project
     * @@return Response indicating whether the service call was successful or
     * errors occured during execution
     */
    public Response handleDeleteProject(Long projectId) {
        /* instantiate new DAO */
        ProjectDAO dao = new ProjectDAO();

        /* remove all files from blobstore and search index */
        ProjectDTO projectDTO = dao.findById(projectId);
        if (projectDTO != null) {
            for (ProjectFileDTO fileDTO : projectDTO.getFiles()) {

                /* remove from blobstore */
                try {
                    blobstoreService.delete(fileDTO.getBlobKey());
                } catch (BlobstoreFailureException bfe) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "couldn't find file in blobstore", bfe);
                    return Response.status(Status.NOT_FOUND).build();
                }

                /* remove from search index */
                SearchIndexManager.INSTANCE.removeDocumentFromIndex(fileDTO.getId());
            }
        }

        /* delete project */
        dao.delete(projectId);
        return Response.noContent().build();
    }

    /**
     * add file in project with given id
     *
     * @param projectId id for the project
     * @param request HttpServletRequest containing form data and the file
     * @param response HttpServletResponse the servlet response
     * @return Response indicating whether the service call was successful or
     * errors occured during execution
     */
    public Response handleCreateFile(Long projectId, HttpServletRequest request, HttpServletResponse response) {

        /* instantiate new DAO */
        ProjectDAO dao = new ProjectDAO();

        /* find project in db */
        ProjectDTO projectDTO = dao.findById(projectId);

        /* log and return error code if project was not found */
        if (projectDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        } else {

            /* create new file in blobstore and get file dto */
            BlobKey blobKey;
            try {
                blobKey = createNewFileInBlobstore("New File", "text/plain", "");
            } catch (IOException ex) {
                Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "couldn't create new file in blobstore", ex);
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }

            /* get blob infos */
            BlobInfo info = infoFactory.loadBlobInfo(blobKey);

            /* create file dto with blobkey of file*/
            ProjectFileDTO fileDTO = new ProjectFileDTO(blobKey, "New File", info.getContentType(), info.getSize(), info.getCreation().getTime(), false);

            /* add file to project */
            projectDTO.addFile(fileDTO);

            /* update project in db */
            projectDTO = dao.update(projectDTO);

            /* 
             * refresh file dto before returning it:
             * update operation generated id in db
             */
            for (ProjectFileDTO dto : projectDTO.getFiles()) {
                if (dto.equals(fileDTO)) {
                    fileDTO = dto;
                }
            }

            /* make file searchable by adding it to the search index */
            SearchIndexManager.INSTANCE.addToIndex(fileDTO);

            /* return response containing created file resource */
            return Response.status(Status.CREATED).entity(fileDTO).build();
        }
    }
    
    /**
     * import file
     *
     * @param projectId id for the project
     * @param request HttpServletRequest containing form data and files
     * @return Response containing the created resource and indicating whether
     * the service call was successful or errors occured during execution
     */
    public Response handleImportFiles(Long projectId, HttpServletRequest request) {

         /* instantiate new DAO */
        ProjectDAO dao = new ProjectDAO();

        /* find project in db */
        ProjectDTO projectDTO = dao.findById(projectId);

        /* log and return error code if project was not found */
        if (projectDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        } else {
            /* store blobs and set files in project */
            Set<ProjectFileDTO> files = storeBlobs(request);
            projectDTO.setFiles(files);

            /* persist new project */
            projectDTO = dao.save(projectDTO);
            
            /* make all files searchable by adding them to the search index */
            SearchIndexManager.INSTANCE.addToIndex(files);
            
            return Response.status(Status.CREATED).entity(projectDTO).build();
        }
    }

    /**
     * get meta data of file with given id in project with given id
     *
     * @param projectId id for the project
     * @param fileId if for the file
     * @@return Response indicating whether the service call was successful or
     * errors occured during execution, containing the file meta data
     */
    public Response handleGetFileMetaData(Long projectId, Long fileId) {
        /* instantiate new DAO */
        ProjectDAO dao = new ProjectDAO();

        /* find project in db */
        ProjectDTO projectDTO = dao.findById(projectId);

        /* log and return error code if project was not found */
        if (projectDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        /* find file and return JSON containing the meta data */
        for (ProjectFileDTO fileDTO : projectDTO.getFiles()) {
            if (fileId.equals(fileDTO.getId())) {
                return Response.ok(fileDTO).build();
            }
        }

        /* if there were not matching project and file ids resource return not found */
        return Response.status(Status.NOT_FOUND).build();
    }

    /**
     * get content of file with given id in project with given id
     *
     * @param projectId id for the project
     * @param fileId if for the file
     * @param response HttpServletResponse that the blob will be added to and
     * serving it
     * @return Response indicating whether the service call was successful or
     * errors occured during execution
     */
    public Response handleGetFileContent(Long projectId, Long fileId, HttpServletResponse response) {
        /* instantiate new DAO */
        ProjectDAO dao = new ProjectDAO();

        /* find project in db */
        ProjectDTO projectDTO = dao.findById(projectId);

        /* log and return error code if project was not found */
        if (projectDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        for (ProjectFileDTO fileDTO : projectDTO.getFiles()) {
            if (fileId.equals(fileDTO.getId())) {

                /* get file from blobstore and serve it */
                BlobKey blobKey = fileDTO.getBlobKey();
                final BlobInfo info = infoFactory.loadBlobInfo(blobKey);
                try {
                    blobstoreService.serve(blobKey, response);
                } catch (IOException ex) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "couldn't find file in blobstore", ex);
                    return Response.status(Status.NOT_FOUND).build();
                }
                
                /* return response and add header */
                return Response.ok().header("Content-Disposition", "attachment; filename=" + info.getFilename()).build();
            }
        }

        /* if there were not matching project and file ids resource return not found */
        return Response.status(Status.NOT_FOUND).build();
    }

    /**
     * set file with given id as main tex in project with given id
     *
     * @param projectId id for the project
     * @param fileId if for the file
     * @param mainTex indicating whether a file should be set as main tex or not
     * @return Response indicating whether the service call was successful or
     * errors occured during execution
     */
    public Response handleSetMainTexFile(Long projectId, Long fileId, boolean mainTex) {
        /* standard response is not modified, will only be overwritten if 
         * file with id was found and maintex attribute changed */
        Response response = Response.notModified().build();

        /* instantiate new DAO */
        ProjectDAO dao = new ProjectDAO();

        /* find project in db */
        ProjectDTO projectDTO = dao.findById(projectId);

        /* log and return error code if project was not found */
        if (projectDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        for (ProjectFileDTO fileDTO : projectDTO.getFiles()) {
            /* update file's main tex entry when found and change reponse to OK with entity */
            if (fileId.equals(fileDTO.getId())) {
                fileDTO.setMainTex(mainTex);
                response = Response.ok().entity(fileDTO).build();
            } else {
                /* remove any previous main tex entries from other files */
                if (fileDTO.isMainTex()) {
                    fileDTO.setMainTex(false);
                }
            }
            /* update entry in search index (add is actually a put) */
            SearchIndexManager.INSTANCE.addToIndex(fileDTO);
        }

        /* persist changes */
        dao.update(projectDTO);

        /* if there were not matching project and file ids resource -> not modified */
        /* if file resource was found -> ok with entity */
        return response;
    }

    /**
     * rename file with given id in project with given id
     *
     * @param projectId id for the project
     * @param fileId if for the file
     * @param name the new name of the file
     * @return Response indicating whether the service call was successful or
     * errors occured during execution
     */
    public Response handleRenameFile(Long projectId, Long fileId, String name) {
        /* instantiate new DAO */
        ProjectDAO dao = new ProjectDAO();

        /* find project in db */
        ProjectDTO projectDTO = dao.findById(projectId);

        /* log and return error code if project was not found */
        if (projectDTO == null || name.isEmpty()) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        /* find and update file in project */
        for (ProjectFileDTO fileDTO : projectDTO.getFiles()) {
            if (fileId.equals(fileDTO.getId())) {

                /* if filename changed delete old file and set new name */
                if (!name.equals(fileDTO.getName())) {
                    fileDTO.setName(name);
                }

                /* finally update last changed date */
                fileDTO.setLastChanged(System.currentTimeMillis());

                /* update entry in search index (add is actually a put) */
                SearchIndexManager.INSTANCE.addToIndex(fileDTO);

                /* persist changes */
                dao.update(projectDTO);

                /* return response containing created file resource */
                return Response.status(Status.OK).entity(fileDTO).build();
            }
        }

        /* if there were not matching project and file ids resource was not modified */
        return Response.notModified().build();
    }

    /**
     * apply patch to original file with given id in project with given id and
     * send updates to all users that have this file currently opened
     *
     * method is in most things very similiar to updateFileContent
     *
     * @param projectId id for the project
     * @param fileId if for the file
     * @param patch patch for the original file as string
     * @param request HttpServletRequest containing the session id
     * @return Response indicating whether the service call was successful or
     * errors occured during execution
     */
    public Response handlePatchFileContent(Long projectId, Long fileId, String patch, HttpServletRequest request) {
        /* instantiate new DAO */
        ProjectDAO dao = new ProjectDAO();

        /* find project in db */
        ProjectDTO projectDTO = dao.findById(projectId);

        /* log and return error code if project was not found */
        if (projectDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        /* find and update file in project */
        for (ProjectFileDTO fileDTO : projectDTO.getFiles()) {
            if (fileId.equals(fileDTO.getId())) {

                /* get file content from blob */
                String content = "";
                try {
                    content = getFileContent(fileDTO.getBlobKey());
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "couldn't get file content for blob " + fileDTO.getBlobKey(), ex);
                    return Response.serverError().build();
                }

                /* get patched content by appling patch to original content */
                String patchedContent = createPatchedContent(patch, content);
                
                /* as there is no such thing as updates for blobs, remove old one and create new */
                try {
                    blobstoreService.delete(fileDTO.getBlobKey());
                } catch (BlobstoreFailureException bfe) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "couldn't find file in blobstore", bfe);
                    return Response.status(Status.NOT_FOUND).build();
                }

                /* store new blob and update blobkey for file */
                BlobKey newBlobKey = null;
                try {
                    newBlobKey = createNewFileInBlobstore(fileDTO.getName(), fileDTO.getContentType(), patchedContent);
                } catch (IOException ex) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "couldn't create file in blobstore", ex);
                    return Response.status(Status.INTERNAL_SERVER_ERROR).build();
                }
                fileDTO.setBlobKey(newBlobKey);

                /* update last changed date */
                fileDTO.setLastChanged(System.currentTimeMillis());

                /* update entry in search index (add is actually a put) */
                SearchIndexManager.INSTANCE.addToIndex(fileDTO);

                /* persist changes */
                dao.update(projectDTO);

                /* send changes to all associated clients except the one that sent the patch */
                pushPatchToAllAssociatedClients(fileId, patch, request.getSession().getId());

                /* return response containing changed file resource */
                return Response.ok().entity(fileDTO).build();
            }
        }

        /* if there was no match return not modified */
        return Response.notModified().build();
    }

    /**
     * get file content from blobstore
     * 
     * @param blobKey the blobkey for the blob
     * @return content as string
     */
    private String getFileContent(BlobKey blobKey) throws UnsupportedEncodingException {
        /* get file content */
        BlobInfo info = infoFactory.loadBlobInfo(blobKey);
        byte[] blobContents = blobstoreService.fetchData(blobKey, 0, info.getSize());
        return new String(blobContents, "UTF-8");
    }

    /**
     * update content for file with given id in project with given id used for
     * files with plain text
     *
     * @param projectId id for the project
     * @param fileId id for the file
     * @param content the file content as string
     * @return Response indicating whether the service call was successful or
     * errors occured during execution
     */
    public Response handleUpdateFileContent(Long projectId, Long fileId, String content) {
        
        /* instantiate new DAO */
        ProjectDAO dao = new ProjectDAO();

        /* find project in db */
        ProjectDTO projectDTO = dao.findById(projectId);

        /* log and return error code if project was not found */
        if (projectDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        /* find and update file in project */
        for (ProjectFileDTO fileDTO : projectDTO.getFiles()) {
            if (fileId.equals(fileDTO.getId())) {

                /* as there is no such thing as updates for blobs, remove old one and create new */
                try {
                    blobstoreService.delete(fileDTO.getBlobKey());
                } catch (BlobstoreFailureException bfe) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "couldn't find file in blobstore", bfe);
                    return Response.status(Status.NOT_FOUND).build();
                }

                /* store new blob and update blobkey for file */
                try {
                    BlobKey newBlobKey = createNewFileInBlobstore(fileDTO.getName(), fileDTO.getContentType(), content);
                    fileDTO.setBlobKey(newBlobKey);
                } catch (IOException ex) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "couldn't create file in blobstore", ex);
                    return Response.status(Status.INTERNAL_SERVER_ERROR).build();
                }

                /* update last changed date */
                fileDTO.setLastChanged(System.currentTimeMillis());

                /* update entry in search index (add is actually a put) */
                SearchIndexManager.INSTANCE.addToIndex(fileDTO);

                /* persist changes */
                dao.update(projectDTO);

                /* return response containing changed file resource */
                return Response.ok().entity(fileDTO).build();
            }
        }

        /* if there was no match return not modified */
        return Response.notModified().build();
    }

    /**
     * update file with given id in project with given id
     *
     * @param projectId id for the project
     * @param fileId if for the file
     * @param request HttpServletRequest containing form data and the file
     * @param response HttpServletResponse the servlet response
     * @return Response indicating whether the service call was successful or
     * errors occured during execution
     */
    public Response handleUpdateFile(Long projectId, Long fileId, HttpServletRequest request, HttpServletResponse response) {
        /* instantiate new DAO */
        ProjectDAO dao = new ProjectDAO();

        /* find project in db */
        ProjectDTO projectDTO = dao.findById(projectId);

        /* log and return error code if project was not found */
        if (projectDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        /* find and update file in project */
        for (ProjectFileDTO fileDTO : projectDTO.getFiles()) {
            if (fileId.equals(fileDTO.getId())) {

                /* as there is no such thing as updates for blobs, remove old one and create new */
                try {
                    blobstoreService.delete(fileDTO.getBlobKey());
                } catch (BlobstoreFailureException bfe) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "couldn't find file in blobstore", bfe);
                    return Response.status(Status.NOT_FOUND).build();
                }

                /* store new blob and update blobkey for file */
                BlobKey newBlobKey = storeBlob(request);
                fileDTO.setBlobKey(newBlobKey);

                /* update last changed date */
                fileDTO.setLastChanged(System.currentTimeMillis());

                /* update entry in search index (add is actually a put) */
                SearchIndexManager.INSTANCE.addToIndex(fileDTO);

                /* persist changes */
                dao.update(projectDTO);

                /* return response containing changed file resource */
                return Response.ok().entity(fileDTO).build();
            }
        }

        /* if there was no match return not modified */
        return Response.notModified().build();
    }

    /**
     * delete file with given id for project with given id
     *
     * @param projectId id for the project
     * @param fileId id for the file
     * @return Response indicating whether the service call was successful or
     * errors occured during execution
     */
    public Response handleDeleteFile(Long projectId, Long fileId) {
        /* instantiate new DAO */
        ProjectDAO dao = new ProjectDAO();

        /* find project in db */
        ProjectDTO projectDTO = dao.findById(projectId);

        /* log and return error code if project was not found */
        if (projectDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        /* find file and remove from the project's file list and blobstore */
        Iterator<ProjectFileDTO> iterator = projectDTO.getFiles().iterator();
        while (iterator.hasNext()) {
            ProjectFileDTO fileDTO = iterator.next();
            if (fileId.equals(fileDTO.getId())) {

                /* remove from blobstore */
                try {
                    blobstoreService.delete(fileDTO.getBlobKey());
                } catch (BlobstoreFailureException bfe) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "couldn't find file in blobstore", bfe);
                    return Response.status(Status.NOT_FOUND).build();
                }

                /* remove from file list */
                iterator.remove();

                /* renove file from search index */
                SearchIndexManager.INSTANCE.removeDocumentFromIndex(fileId);
            }
        }

        /* update project in db (orphanremoval=true in Job.class will cascade delete file)*/
        dao.update(projectDTO);

        return Response.noContent().build();
    }

    /**
     * search in search index for query and return results
     *
     * @param query string containing the query/search term(s)
     * @return Response indicating whether the service call was successful or
     * errors occured during execution
     */
    public Response handleSearch(String query) {
        List<ProjectFileDTO> foundFiles = new ArrayList<>();

        /* check of the search is not empty */
        if (query != null && !query.isEmpty()) {
            Results<ScoredDocument> results = SearchIndexManager.INSTANCE.retrieveDocuments(query.trim());

            /* get attributes for each found document */
            for (ScoredDocument scoredDocument : results) {
                Key key = KeyFactory.createKey(Project.class.getSimpleName(), scoredDocument.getOnlyField("key").getText());
                BlobKey blobKey = new BlobKey(scoredDocument.getOnlyField("blobKey").getText());
                String name = scoredDocument.getOnlyField("name").getText();
                String contentType = scoredDocument.getOnlyField("contentType").getText();
                long size = Long.valueOf(scoredDocument.getOnlyField("size").getText());
                long lastChanged = Long.valueOf(scoredDocument.getOnlyField("lastChanged").getText());
                boolean mainTex = Boolean.valueOf(scoredDocument.getOnlyField("mainTex").getText());

                /* create file dto and add to found list */
                ProjectFileDTO fileDTO = new ProjectFileDTO(blobKey, name, contentType, size, lastChanged, mainTex);
                fileDTO.setKey(key);
                foundFiles.add(fileDTO);
            }
        }

        /* when nothing could be found there will simpy be an empty list returned */
        return Response.ok().entity(foundFiles).build();
    }

    /**
     * get token from Channel Service
     *
     * @param request HttpServletRequest containing the session id that is used
     * to create a token for the client
     * @return Response containing the token
     */
    public Response handleGetToken(HttpServletRequest request) {
        HttpSession session = request.getSession();

        /* check if session id is not empty */
        if (session == null || session.getId().isEmpty()) {
            return Response.serverError().build();
        }

        /* return the created token */
        String token = channelService.createChannel(session.getId());
        return Response.status(Status.CREATED).entity(token).build();
    }

    /**
     * register client in SessionManager for an open document with given file id
     *
     * @param fileId id for the file
     * @param request HttpServletRequest containing the session id that is used
     * to create a token for the client
     * @return Response indicating whether the service call was successful or
     * errors occured during execution
     */
    public Response handleRegisterForDocument(Long fileId, HttpServletRequest request) {
        SessionManager.setNewOpenedDocument(request.getSession().getId(), fileId);
        return Response.ok().build();
    }
    
    /**
     * unregister client in SessionManager for an open document
     *
     * @param fileId id for the file
     * @param request HttpServletRequest containing the session id that is used
     * to create a token for the client
     * @return Response indicating whether the service call was successful or
     * errors occured during execution
     */
    public Response handleUnregisterForDocument(Long fileId, HttpServletRequest request) {
        boolean removed = SessionManager.removeFromOpenedDocument(request.getSession().getId(), fileId);
        if (removed) {
            return Response.ok().build();
        } else {
            return Response.notModified().build();
        }
    }
    
    /**
     * apply patch to content to get patched content
     * 
     * @param patch the patch create the updated content
     * @param content the old editor content prior to the editing
     * @return patchContent the updated/patched content and therefore being the current editor content
     * @throws IllegalArgumentException 
     */
    private String createPatchedContent(String patch, String content) throws IllegalArgumentException {
        /* create diff match patch instance */
        diff_match_patch dmp = new diff_match_patch();
        
        /* create single patches from text patch and apply to file content */
        List<Patch> patches = dmp.patch_fromText(patch);
        Object[] results = dmp.patch_apply((LinkedList<Patch>) patches, content);
        
        /* first array entry contains patched text  */
        return (String) results[0];
    }
    
    /**
     * send changes to all associated clients except the one that sent the patch
     * 
     * @param fileId id of the openend file
     * @param patch the patch for the editor content as string
     * @param originSessionId session id of the client that sent the patch
     */
    private void pushPatchToAllAssociatedClients(Long fileId, String patch, String originSessionId) {
        
        /* get map of all opened documents */
        Map<String, Long> openedDocuments = SessionManager.getOpenedDocuments();
        for (Entry<String, Long> entry : openedDocuments.entrySet()) {
            
            /* get session and document id */
            String sessionId = entry.getKey();
            Long documentId = entry.getValue();
            
            /* send patch to client if it has the same file opened and it was NOT 
               the one who created and sent the patch in the first place */
            if (documentId.equals(fileId) && !sessionId.equals(originSessionId)){
                channelService.sendMessage(new ChannelMessage(sessionId, patch));
            }
        }
    }

    /**
     * store blob and return blobkey
     *
     * @return blobKey the blob key under which the blob is stored in the
     * blobstore
     */
    private BlobKey storeBlob(HttpServletRequest request) {
        /* blobs are returned from request as map, although there is just one entry in it containing a list with all blobs */
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);

        /* there is only one entry with a blob */
        if (blobs.entrySet().iterator().hasNext()) {
            Map.Entry<String, List<BlobKey>> entry = blobs.entrySet().iterator().next();

            /* the actual files are contained in the list with blobkeys */
            BlobKey blobKey = entry.getValue().get(0);

            /* get blob infos */
            BlobInfo info = infoFactory.loadBlobInfo(blobKey);
            
            /* build preview when file is image */
            if (info.getContentType().startsWith("image")) {
                ImagesService imagesService = ImagesServiceFactory.getImagesService();
                imagesService.getServingUrl(ServingUrlOptions.Builder.withBlobKey(blobKey)
                        .crop(true).imageSize(200));
            }

            /* return new blobkey */
            return blobKey;
        } else {
            return null;
        }
    }

    /**
     * store blobs and return collection of files
     *
     * @return files set containing all files with their infos as well as
     * blobkeys
     */
    private Set<ProjectFileDTO> storeBlobs(HttpServletRequest request) {
        Set<ProjectFileDTO> files = new HashSet<>();

        /* blobs are returned from request as map, although there is just one entry in it containing a list with all blobs */
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);

        /* get entry with list of blobkeys */
        for (Map.Entry<String, List<BlobKey>> entry : blobs.entrySet()) {

            /* the actual files are contained in the list with blobkeys */
            for (BlobKey blobKey : entry.getValue()) {

                /* get blob infos */
                BlobInfo info = infoFactory.loadBlobInfo(blobKey);

                /* build preview when file is image */
                if (info.getContentType().startsWith("image")) {
                    ImagesService imagesService = ImagesServiceFactory.getImagesService();
                    imagesService.getServingUrl(ServingUrlOptions.Builder.withBlobKey(blobKey)
                            .crop(true).imageSize(200));
                }
                
                /* create file DTO with file meta data and add to project */
                ProjectFileDTO fileDTO = new ProjectFileDTO(blobKey, info.getFilename(), info.getContentType(), info.getSize(), System.currentTimeMillis(), false);
                
                
                /* if tex(t) file, try to detect if it is main tex that will be the compilation target */
                if(info.getContentType().contains("tex")){
                    if(hasBeginDocument(blobKey)){
                        fileDTO.setMainTex(true);
                    }
                }
                
                /* add to files*/
                files.add(fileDTO);
            }
        }
        return files;
    }

    /**
     * create new, empty text file in blobstore
     *
     * SHOULD BE REPLACED AS SOON A POSSIBLE, USED API IS DEPRECATED, BUT AT THE
     * MOMENT THERE SEEMS TO BE NO OTHER WAY TO PUT FILES WITHOUT REQUEST/USER
     * INTERACTION TO BLOBSTORE
     *
     * @return blobKey blobKey for the new text file
     * @throws IOException
     */
    @Deprecated
    private BlobKey createNewFileInBlobstore(String filename, String contextType, String content) throws IOException {
        /* get a file service */
        FileService fileService = FileServiceFactory.getFileService();

        /* create a new Blob file with mime-type "text/plain" */
        AppEngineFile file = fileService.createNewBlobFile(contextType, filename);
        
        /* if no write channel was opened and closed no file will actually be
         created and the blobkey will be null returned by the API */
        FileWriteChannel writeChannel = fileService.openWriteChannel(file, true);

        /* write to file */
        writeChannel.write(ByteBuffer.wrap(content.getBytes()));

        /* close and finalise */
        writeChannel.closeFinally();

        /* now get the blobkey using the Blobstore API */
        BlobKey blobKey = fileService.getBlobKey(file);
        return blobKey;
    }
    
    /**
     * get file content and search for \begin{document},
     * indicating this is the file that must be finally compiled
     * 
     * @param blobKey blobKey for the tex(t) file
     * @return true if document contains \begin{document}
     */
    private boolean hasBeginDocument(BlobKey blobKey){
        String content = "";
        try {
            content = getFileContent(blobKey);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "couldn't get file content", ex);
        }
        return content.contains("\\begin{document}");
    }
}
