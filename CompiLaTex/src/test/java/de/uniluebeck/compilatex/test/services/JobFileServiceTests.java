package de.uniluebeck.compilatex.test.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.response.Response;
import de.uniluebeck.compilatex.test.TestBase;
import de.uniluebeck.compilatex.test.data.TestDataProvider;
import java.io.File;
import java.util.logging.Logger;
import javax.ws.rs.core.Response.Status;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class JobFileServiceTests extends TestBase {

    public static final Logger LOG = Logger.getLogger(JobFileServiceTests.class.getName());
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testAddFile(File file) {
        /* create job and save id */
        Long jobId = RestHelper.createJob();

        /* post file for job and assert resource was successfully created */
        Response response = RestHelper.addFileForJob(jobId, file, false);
        Long fileId = response.jsonPath().getLong("id");
        assertNotNull("created resource didn't not return any id or was empty", fileId);
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testAddFileToNonExistingJob(File file) {
        given().
            multiPart(file).
            param("filename", file.getName()).
            param("isMainTex", false).
        expect().
            statusCode(Status.BAD_REQUEST.getStatusCode()).log().all().
        when().
            post("/jobs/12345/files/");
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testGetLastChanged(File file) {
        /* create job and save id */
        Long jobId = RestHelper.createJob();

        /* add file for job, assert resource contains id */
        Long fileId = RestHelper.addFileForJobGetId(jobId, file, false);
        assertNotNull("created resource didn't not return any id or was empty!", fileId);

        /* get last changed date and assert it is not empty */
        String lastChanged = expect().
            statusCode(Status.OK.getStatusCode()).log().all().
        when().
            get("/jobs/"+jobId+"/files/"+fileId+"/lastchanged").asString();
        assertFalse("created resource didn't not return any last changed date or was empty!", lastChanged.isEmpty());
    }
    
    @Test
    public void testGetLastChangedOfNonExistingJob() {
        /* get last changed date and assert it is not empty */
        expect().
            statusCode(Status.BAD_REQUEST.getStatusCode()).log().all().
        when().
            get("/jobs/12345/files/12345/lastchanged").asString();
    }
    
    @Test
    public void testGetLastChangedOfNonExistingFile() {
        /* create job and save id */
        Long jobId = RestHelper.createJob();

        /* get last changed date and assert it is not empty */
        expect().
            statusCode(Status.BAD_REQUEST.getStatusCode()).log().all().
        when().
            get("/jobs/"+jobId+"/files/12345/lastchanged").asString();
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testUpdateFile(File file) {
        long testStart = System.currentTimeMillis();
        
        /* create job and save id */
        Long jobId = RestHelper.createJob();

        /* add file for job */
        Long fileId = RestHelper.addFileForJobGetId(jobId, file, false);
        assertNotNull("created resource didn't not return any id or was empty", fileId);
        
        /* update file with same file */
        Response response = given().
            multiPart(file).
            param("filename", file.getName()).
        expect().
            statusCode(Status.OK.getStatusCode()).log().all().
        when().
            put("/jobs/"+jobId+"/files/"+fileId);
        
        /* assert updated file has same id */
        Long updatedFileId = response.jsonPath().getLong("id");
        assertNotNull("created resource didn't not return any id or was empty", updatedFileId);
  
        /* updated file modification date must be newer than time when the test was started */
        long updatedFileDate = response.jsonPath().getLong("lastChanged");
        assertTrue("upated file modification date is not newer!", testStart < updatedFileDate);
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testUpdateFileInNonExistingJob(File file) {
        given().
            multiPart(file).
            param("filename", file.getName()).
        expect().
            statusCode(Status.BAD_REQUEST.getStatusCode()).log().all().
        when().
            put("/jobs/12345/files/12345");
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testUpdateNonExistingFile(File file) {
        /* create job and save id */
        Long jobId = RestHelper.createJob();

        /**
         * when trying to update a non existing file,
         * expect status code to be CREATED as it will 
         * be handled by the server as new resource
         */
        Response response = given().
            multiPart(file).
            param("filename", file.getName()).
        expect().
            statusCode(Status.CREATED.getStatusCode()).log().all().
        when().
            put("/jobs/"+jobId+"/files/12345");
        
        /* also, resource including id will be returned */
        Long fileId = response.jsonPath().getLong("id");
        assertNotNull("created resource didn't not return any id or was empty", fileId);
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testDeleteFile(File file) {
        /* create job and save id */
        Long jobId = RestHelper.createJob();

        /* add file for job */
        Long fileId = RestHelper.addFileForJobGetId(jobId, file, false);
        assertNotNull("created resource didn't not return any id or was empty", fileId);
        
        /* delete file */
        expect().
            statusCode(Status.NO_CONTENT.getStatusCode()).log().all().
        when().
            delete("/jobs/"+jobId+"/files/"+fileId);
    }
    
    @Test
    public void testDeleteNonExistingFile() {
        /* create job and save id */
        Long jobId = RestHelper.createJob();

        /* try to delete non existing file */
        expect().
            statusCode(Status.NO_CONTENT.getStatusCode()).log().all().
        when().
            delete("/jobs/"+jobId+"/files/12345");
    }
}
