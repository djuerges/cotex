package de.uniluebeck.compilatex.test.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import static com.jayway.restassured.RestAssured.expect;
import de.uniluebeck.compilatex.test.TestBase;
import java.util.logging.Logger;
import javax.ws.rs.core.Response.Status;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class BasicJobServiceTests extends TestBase {

    public static final Logger LOG = Logger.getLogger(BasicJobServiceTests.class.getName());
    
    @Test
    public void testCreateJob() {
        for (int i = 1; i <= 10; i++) {
            expect().
                statusCode(Status.CREATED.getStatusCode()).log().all().
            when().
                post("/jobs").
            then().
                body("directory", Matchers.anything()).body("files", Matchers.empty());
        }
    }
    
    @Test
    public void testDeleteJob() {
        /* create job and save id */
        Long jobId = RestHelper.createJob();

        /* delete job */
        expect().
            statusCode(Status.NO_CONTENT.getStatusCode()).log().all().
        when().
            delete("/jobs/"+jobId);
    }
    
    @Test
    public void testDeleteNonExistingJob() {
        /* try to delete non-existing job */
        expect().
            statusCode(Status.NO_CONTENT.getStatusCode()).log().all().
        when().
            delete("/jobs/12345");
    }
}
