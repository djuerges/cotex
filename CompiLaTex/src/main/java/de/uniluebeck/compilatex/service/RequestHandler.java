/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.compilatex.service;

import de.uniluebeck.compilatex.PersistenceManager;
import de.uniluebeck.compilatex.dto.JobFileDTO;
import de.uniluebeck.compilatex.dto.JobDTO;
import de.uniluebeck.compilatex.shell.Shell;
import de.uniluebeck.compilatex.utilities.FileOperations;
import de.uniluebeck.compilatex.utilities.LatexEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.eclipse.persistence.sessions.serializers.JSONSerializer;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class RequestHandler extends JSONSerializer {

    /**
     * static to prevent DB operations by concurrent instances 
     */
    static PersistenceManager pm = new PersistenceManager();
    
    /**
     * get installed latex environments on server
     * @return all available latex environments
     */
    public Response handleGetLatexEnvironments() {
        return Response.ok().entity(LatexEnvironment.values()).build();
    }

    /**
     * create new job
     * @return Response containing the created resource and indicating whether the service call was successful or errors occured during execution
     */
    public Response handleCreateJob() {
        /* create unique directory name from uuid and save new job */
        JobDTO jobDTO = new JobDTO();
        jobDTO.setDirectory(UUID.randomUUID().toString());
        jobDTO = pm.save(jobDTO);
        return Response.status(Status.CREATED).entity(jobDTO).build();
    }
    
    /**
     * get (most recent) log (of last compilation) for job given id
     * @param jobId id for the job
     * @param latexEnvironment latex environment used for compilation as string
     * @return Response indicating whether the service call was successful or errors occured during execution
     */
    public Response handleCompile(Long jobId, String latexEnvironment) {
        LatexEnvironment environment = LatexEnvironment.valueOfName(latexEnvironment);
        String filename = "", parentFolder = "";

        /* find job in db */
        JobDTO jobDTO = pm.find(jobId);

        /* log and return error code if job was not found */
        if(jobDTO == null){
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "job dto was actually not found");
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        /* find main file and get parent folder and filename */
        for(JobFileDTO jobFileDTO : jobDTO.getFiles()){
            if(jobFileDTO.isMainTex()){
                filename = jobFileDTO.getName();
                parentFolder = jobFileDTO.getParentFolder();
            }
        }
        
        /* compile if environment is installed */
        if(Shell.isInstalled(environment)){
            try {
                Logger.getLogger(RequestHandler.class.getName()).log(Level.INFO, "trying to compile {0} in folder {1} with {2}", new Object[]{filename, parentFolder, latexEnvironment});
                Shell.compile(environment, parentFolder, filename);
            } catch (IOException ex) {
                Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "could not compile " + filename + " in folder " + parentFolder + " with " + latexEnvironment, ex);
            }
            
            return Response.ok().build();
        } else {
            return Response.status(Status.NOT_IMPLEMENTED).build();
        }
    }

    /**
     * get (most recent) log (of last compilation) for job given id
     * @param jobId id for the job
     * @return (most recent) log (of last compilation) as string
     */
    public Response handleGetLog(Long jobId) {
        /* find job in db */
        JobDTO jobDTO = pm.find(jobId);

        /* log and return error code if job was not found */
        if(jobDTO == null){
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "job dto was actually not found");
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        try {
            String log = FileOperations.getLog(jobDTO);
            return Response.ok().entity(log).build();
        } catch (IOException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "could not read from file", ex);
            return Response.serverError().build();
        }
    }
    
    /**
     * get (most recent) log (of last compilation) for job given id
     * with HTML annotations for warnings and errors and linebreaks
     * 
     * @param jobId id for the job
     * @return (most recent) log (of last compilation) as string with HTML annotations
     */
    public Response handleGetHtmlLog(Long jobId) {
        /* find job in db */
        JobDTO jobDTO = pm.find(jobId);

        /* log and return error code if job was not found */
        if(jobDTO == null){
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "job dto was actually not found");
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        try {
            String log = FileOperations.getHtmlLog(jobDTO);
            return Response.ok().entity(log).build();
        } catch (IOException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "could not read from file", ex);
            return Response.serverError().build();
        }
    }

    /**
     * get compiled pdf for job given id
     * @param jobId id for the job
     * @return pdf as file
     */
    public Response handleGetPdf(Long jobId) {
        /* find job in db */
        JobDTO jobDTO = pm.find(jobId);

        /* log and return error code if job was not found */
        if(jobDTO == null){
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "job dto was actually not found");
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        File pdf = FileOperations.getPdf(jobDTO);
        return Response.ok(pdf).header("Content-Disposition", "attachment; filename=" + pdf.getName()).build();
    }

    /**
     * delete job with given id
     * @param jobId id for the job
     * @@return Response indicating whether the service call was successful or errors occured during execution 
     */
    public Response handleDeleteJob(Long jobId) {
        pm.delete(jobId);
        return Response.noContent().build();
    }

    /**
     * add file in job with given id
     * @param jobId id for the job
     * @param isMainTex true if this is the main file for the project marking it as compilation target
     * @param filename the name of the file to be added
     * @param fileInputStream the actual file as input stream
     * @return Response indicating whether the service call was successful or errors occured during execution
     */
    public Response handleAddFile(Long jobId, boolean isMainTex, String filename, InputStream fileInputStream) {
        /* find job in db */
        JobDTO jobDTO = pm.find(jobId);

        /* log and return error code if job was not found */
        if(jobDTO == null){
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "job dto was actually not found");
            return Response.status(Status.BAD_REQUEST).build();
        } else {
            /* write file to project dir */
            Path path = null;
            try {
                path = FileOperations.saveFile(jobDTO.getDirectory(), filename, fileInputStream);
            } catch (IOException ex) {
                Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "couldn't write file to job directory", ex);
                return Response.serverError().build();
            }
            
             /* create and add file to job*/
            JobFileDTO fileDTO = new JobFileDTO(filename, path.getParent().toString(), System.currentTimeMillis(), isMainTex);
            jobDTO.addFile(fileDTO);

            /* update job in db */
            jobDTO = pm.update(jobDTO);
            
            /* 
             * refresh file dto before returning it:
             * update operation generated id in db
             */
            for(JobFileDTO dto : jobDTO.getFiles()){
                if(dto.equals(fileDTO)){
                    fileDTO = dto;
                }
            }
            
            /* return response containing created file resource */
            return Response.status(Status.CREATED).entity(fileDTO).build();
        }
    }

    /**
     * update file with given id in job with given id
     * @param jobId id for the job
     * @param fileId if for the file
     * @param filename the name of the file to be added
     * @param fileInputStream the actual file as input stream
     * @return Response indicating whether the service call was successful or errors occured during execution
     */
    public Response handleUpdateFile(Long jobId, Long fileId, String filename, InputStream fileInputStream) {
        /* find job in db */
        JobDTO jobDTO = pm.find(jobId);

        /* log and return error code if job was not found */
        if(jobDTO == null){
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "job dto was actually not found");
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        /* find and update file in job */
        for (JobFileDTO fileDTO : jobDTO.getFiles()) {
            if (fileId.equals(fileDTO.getId())) {

                /* if filename changed delete old file and set new name */
                if (!filename.equals(fileDTO.getName())) {
                    Path path = FileOperations.getPathToFile(jobDTO.getDirectory(), fileDTO.getName());
                    try {
                        FileOperations.deleteFile(path);
                    } catch (IOException ex) {
                        Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "couldn't delete file from job directory", ex);
                        return Response.serverError().build();
                    }
                    fileDTO.setName(filename);
                }
                
                /* write file to project dir */
                try {
                    FileOperations.saveFile(jobDTO.getDirectory(), filename, fileInputStream);
                } catch (IOException ex) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "couldn't write file to job directory", ex);
                    return Response.serverError().build();
                }
                
                /* finally update last changed date */
                fileDTO.setLastChanged(System.currentTimeMillis());
                
                /* persist changes */
                pm.update(jobDTO);

                /* return response containing created file resource */
                return Response.status(Status.OK).entity(fileDTO).build();
            }
        }
        
        /* if there was no match, file will be consired as new */
        return handleAddFile(jobId, false, filename, fileInputStream);
    }

    
    /**
     * get last modification date for file with given id in job with given id
     * @param jobId id for the job
     * @param fileId id for the file
     * @return long when the file was changed last time
     */
    public Response handleGetLastFileModificationDate(Long jobId, Long fileId) {
        /* set response not found as default */
        Response response = Response.status(Status.BAD_REQUEST).entity(0L).build();
        
        /* find job in db */
        JobDTO jobDTO = pm.find(jobId);

        /* log and return error code if job was not found */
        if(jobDTO == null){
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "job dto was actually not found");
            return Response.status(Status.BAD_REQUEST).build();
        }

        /* find file and return the last changed date */
        for (JobFileDTO fileDto : jobDTO.getFiles()) {
            if (fileId.equals(fileDto.getId())) {
                /* overwrite response if correct file was found */
                response = Response.ok().entity(fileDto.getLastChanged()).build();
            }
        }

        /* return not found as default if file with id was not found */
        return response;
    }

    /**
     * delete file with given id for job with given id
     * @param jobId id for the job
     * @param fileId id for the file
     * @return Response indicating whether the service call was successful or errors occured during execution
     */
    public Response handleDeleteFile(Long jobId, Long fileId) {
        /* find job in db */
        JobDTO jobDTO = pm.find(jobId);

        /* log and return error code if job was not found */
        if(jobDTO == null){
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, "job dto was actually not found");
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        /* find file and remove from the project's file list */
        Iterator<JobFileDTO> iterator = jobDTO.getFiles().iterator();
        while(iterator.hasNext()){
            JobFileDTO fileDto = iterator.next();
            if (fileId.equals(fileDto.getId())) {
                iterator.remove();
            }
        }
        
        /* update job in db (orphanremoval=true in Job.class will cascade delete file)*/
        pm.update(jobDTO);
        
        return Response.noContent().build();
    }
}