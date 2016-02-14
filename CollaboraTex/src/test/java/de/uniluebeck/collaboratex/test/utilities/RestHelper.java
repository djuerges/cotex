package de.uniluebeck.collaboratex.test.utilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.appengine.api.datastore.Key;
import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.response.Response;
import java.io.File;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class RestHelper {

    /**
     * post to rest service to create new project
     * @return id of the created project
     */
    public static long createProject(){
        return expect().
            statusCode(Status.CREATED.getStatusCode()).log().all().
        when().
            post("/projects").jsonPath().getLong("id");
    }

    /**
     * add given file for project with given id
     * @param projectId id for the project
     * @param file the file that will be posted to the project
     * @param isMainTex is file the main file and target for compilation
     * @return id of the created file
     */
    public static long addFileForProjectGetId(Long projectId, File file, boolean isMainTex) {
        return addFileForProject(projectId, file, isMainTex).jsonPath().getLong("id");
    }
    
    /**
     * add given file for project with given id
     * @param projectId id for the project
     * @param file the file that will be posted to the project
     * @param isMainTex is file the main file and target for compilation
     * @return server's response of the REST call
     */
    public static Response addFileForProject(Long projectId, File file, boolean isMainTex) {
        return given().
            multiPart(file).
            param("mainTex", isMainTex).
        expect().
            statusCode(Status.CREATED.getStatusCode()).//log().all().
        when().
            post("/projects/"+projectId+"/files/");
    }
}
