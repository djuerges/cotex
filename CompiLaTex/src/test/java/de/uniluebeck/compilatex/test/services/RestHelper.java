package de.uniluebeck.compilatex.test.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.response.Response;
import de.uniluebeck.compilatex.utilities.LatexEnvironment;
import java.io.File;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class RestHelper {

    /**
     * post to rest service to create new job
     * @return id of the created job
     */
    public static Long createJob(){
        return expect().
            statusCode(Status.CREATED.getStatusCode()).log().all().
        when().
            post("/jobs").getBody().jsonPath().getLong("id");
    }

    /**
     * add given file for job with given id
     * @param jobId id for the job
     * @param file the file that will be posted to the job
     * @param isMainTex is file the main file and target for compilation
     * @return id of the created file
     */
    public static Long addFileForJobGetId(Long jobId, File file, boolean isMainTex) {
        return addFileForJob(jobId, file, isMainTex).jsonPath().getLong("id");
    }
    
    /**
     * add given file for job with given id
     * @param jobId id for the job
     * @param file the file that will be posted to the job
     * @param isMainTex is file the main file and target for compilation
     * @return server's response of the REST call
     */
    public static Response addFileForJob(Long jobId, File file, boolean isMainTex) {
        return given().
            multiPart(file).
            param("filename", file.getName()).
            param("isMainTex", isMainTex).
        expect().
            statusCode(Status.CREATED.getStatusCode()).log().all().
        when().
            post("/jobs/"+jobId+"/files/");
    }
    
    /**
     * compile job to create pdf
     * @param jobId id for the job
     * @param environment latex environment/compiler that will be used for compilation
     * @return server's response of the REST call
     */
    public static Response compileJob(Long jobId, LatexEnvironment environment){
        return expect().
                statusCode(Status.OK.getStatusCode()).log().all().
            when().
                get("/jobs/"+jobId+"/compile/"+environment.name());
    }
}
