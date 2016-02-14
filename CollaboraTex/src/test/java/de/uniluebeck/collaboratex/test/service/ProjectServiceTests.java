package de.uniluebeck.collaboratex.test.service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import de.uniluebeck.collaboratex.test.utilities.RestHelper;
import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import de.uniluebeck.collaboratex.test.TestBase;
import java.util.logging.Logger;
import javax.ws.rs.core.Response.Status;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class ProjectServiceTests extends TestBase {

    public static final Logger LOG = Logger.getLogger(ProjectServiceTests.class.getName());
    
    @Test
    public void testCreateProject() {
        for (int i = 1; i <= 10; i++) {
            expect().
                statusCode(Status.CREATED.getStatusCode()).log().all().
            when().
                post("/projects").
            then().
                body("name", Matchers.anything()).body("files", Matchers.empty());
        }
    }
    
    @Test
    public void testRenameProject() {
        /* create project and save id */
        long projectId = RestHelper.createProject();

        /* rename project */
        given().
            param("value", "newname").
        expect().
            statusCode(Status.OK.getStatusCode()).log().all().
        when().
            post("/projects/"+projectId+"/rename").
        then().
            body("name", Matchers.is("newname"));
    }
    
    @Test
    public void testRenameNonExistingProject() {
        /* rename project */
        given().
            param("projectname", "newname").
        expect().
            statusCode(Status.BAD_REQUEST.getStatusCode()).log().all().
        when().
            post("/projects/12345/rename");
    }
    
    @Test
    public void testDeleteProject() {
        /* create project and save id */
        long projectId = RestHelper.createProject();

        /* delete project */
        expect().
            statusCode(Status.NO_CONTENT.getStatusCode()).log().all().
        when().
            delete("/projects/"+projectId);
    }
    
    @Test
    public void testDeleteNonExistingProject() {
        /* try to delete non-existing project */
        expect().
            statusCode(Status.NO_CONTENT.getStatusCode()).log().all().
        when().
            delete("/projects/12345");
    }
}