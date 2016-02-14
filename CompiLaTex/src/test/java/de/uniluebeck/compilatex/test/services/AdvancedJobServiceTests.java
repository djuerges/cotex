package de.uniluebeck.compilatex.test.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import static com.jayway.restassured.RestAssured.expect;
import de.uniluebeck.compilatex.test.TestBase;
import de.uniluebeck.compilatex.test.data.TestDataProvider;
import de.uniluebeck.compilatex.utilities.FileOperations;
import de.uniluebeck.compilatex.utilities.LatexEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class AdvancedJobServiceTests extends TestBase {

    public static final Logger LOG = Logger.getLogger(AdvancedJobServiceTests.class.getName());
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testCompile(File file) {
        /* create job and save id */
        Long jobId = RestHelper.createJob();

        /* add file for job */
        Long fileId = RestHelper.addFileForJobGetId(jobId, file, true);
        assertNotNull("created resource didn't not return any id or was empty", fileId);
        
        /* call compile service with every available latex environment */
        for(LatexEnvironment environment : LatexEnvironment.values()){
            RestHelper.compileJob(jobId, environment);
        }
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "broken-tex-file")
    public void testBrokenCompile(File file) {
        testCompile(file);
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testGetPdf(File file) {
        /* create job and save id */
        Long jobId = RestHelper.createJob();

        /* add file for job */
        Long fileId = RestHelper.addFileForJobGetId(jobId, file, true);
        assertNotNull("created resource didn't not return any id or was empty", fileId);
        
        /* compile job */
        RestHelper.compileJob(jobId, LatexEnvironment.PDFLATEX);
        
        /* call service and assert pdf was returned */
        InputStream inputStream = expect().statusCode(Response.Status.OK.getStatusCode()).log().ifError().
                                when().get("/jobs/"+jobId +"/pdf").getBody().asInputStream();
        
        /* save stream as test file */
        Path path = null;
        try {
            path = FileOperations.saveFile(".", "test.pdf", inputStream);
        } catch (IOException ex) {
            Logger.getLogger(RestHelper.class.getName()).log(Level.SEVERE, "could not save stream", ex);
        }
        
        /* assert file exists and has content */
        assertTrue("file not found!", Files.exists(path));
        assertTrue("file length was 0!", path.toFile().length() > 0);

        /* clean up and delete file */
        try {
            Files.delete(path);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "could not delete file", ex);
        }
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "broken-tex-file")
    public void testGetBrokenPdf(File file) {
        testGetPdf(file);
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testGetLog(File file) {
        /* create job and save id */
        Long jobId = RestHelper.createJob();

        /* add file for job */
        Long fileId = RestHelper.addFileForJobGetId(jobId, file, true);
        assertNotNull("created resource didn't not return any id or was empty", fileId);
        
        /* compile job */
        RestHelper.compileJob(jobId, LatexEnvironment.PDFLATEX);
        
        /* get log and assert it is not empty */
        String log = expect().statusCode(200).log().all().
                when().get("/jobs/"+jobId+"/log").getBody().asString();
        assertFalse("log is empty!", log.isEmpty());
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "broken-tex-file")
    public void testGetBrokenLog(File file) {
        testGetLog(file);
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testGetHtmlLog(File file) {
        /* create job and save id */
        Long jobId = RestHelper.createJob();

        /* add file for job */
        Long fileId = RestHelper.addFileForJobGetId(jobId, file, true);
        assertNotNull("created resource didn't not return any id or was empty", fileId);
        
        /* compile job */
        RestHelper.compileJob(jobId, LatexEnvironment.PDFLATEX);
        
        /* get html log and assert it is not empty */
        String log = expect().statusCode(200).log().all().
                when().get("/jobs/"+jobId+"/log/html").getBody().asString();
        assertFalse("log is empty!", log.isEmpty());
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "broken-tex-file")
    public void testGetBrokenHtmlLog(File file) {
        testGetHtmlLog(file);
    }
}
