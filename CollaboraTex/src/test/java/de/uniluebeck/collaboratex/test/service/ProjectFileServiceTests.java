package de.uniluebeck.collaboratex.test.service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import de.uniluebeck.collaboratex.test.utilities.RestHelper;
import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.response.Response;
import de.uniluebeck.collaboratex.algorithm.diff_match_patch;
import de.uniluebeck.collaboratex.algorithm.diff_match_patch.Diff;
import de.uniluebeck.collaboratex.algorithm.diff_match_patch.Patch;
import de.uniluebeck.collaboratex.test.TestBase;
import de.uniluebeck.collaboratex.test.utilities.TestDataProvider;
import java.io.File;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;
import org.hamcrest.Matchers;
import static org.mockito.Mockito.mock;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class ProjectFileServiceTests extends TestBase {

    public static final Logger LOG = Logger.getLogger(ProjectFileServiceTests.class.getName());

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testAddFile(File file) {
        /* create project and save id */
        long projectId = RestHelper.createProject();

        /* post file for project and assert resource was successfully created */
        long fileId = RestHelper.addFileForProjectGetId(projectId, file, false);
        assertNotNull("created resource didn't not return any id or was empty", fileId);
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testAddFileToNonExistingProject(File file) {
        given().
            multiPart(file).
            param("mainTex", false).
        expect().
            statusCode(Status.BAD_REQUEST.getStatusCode()).log().all().
        when().
            post("/projects/12345/files/");
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testRenameFile(File file) {
        /* create project and save id */
        long projectId = RestHelper.createProject();

        /* post file for project and assert resource was successfully created */
        long fileId = RestHelper.addFileForProjectGetId(projectId, file, false);
        assertNotNull("created resource didn't not return any id or was empty", fileId);
        
        /* rename file */
        given().
            param("value", "newname").
        expect().
            statusCode(Status.OK.getStatusCode()).log().all().
        when().
            post("/projects/"+projectId+"/files/"+fileId+"/rename").
        then().
            body("name", Matchers.is("newname"));
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testSetAsMainTexFile(File file) {
        /* create project and save id */
        long projectId = RestHelper.createProject();

        /* post file for project and assert resource was successfully created */
        long fileId = RestHelper.addFileForProjectGetId(projectId, file, false);
        assertNotNull("created resource didn't not return any id or was empty", fileId);
        
        /* set file as main tex */
        given().
            param("mainTex", true).
        expect().
            statusCode(Status.OK.getStatusCode()).log().all().
        when().
            post("/projects/" + projectId + "/files/" + fileId + "/setmaintex").
        then().
            body("mainTex", Matchers.is(true));
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testGetFileMetaData(File file) {
        /* create project and save id */
        long projectId = RestHelper.createProject();

        /* post file for project and assert resource was successfully created */
        Response response = RestHelper.addFileForProject(projectId, file, false);
        Long fileId = response.jsonPath().getLong("id");
        String name = response.jsonPath().get("name");
        assertNotNull("created resource didn't not return any id or was empty", fileId);
        
        /* get file meta data */
        String nameFromResponse = expect().
            statusCode(Status.OK.getStatusCode()).log().all().
        when().
            get("/projects/"+projectId+"/files/"+fileId+"/meta").jsonPath().getString("name");
        assertTrue(nameFromResponse.contains(name));
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testGetFileContent(File file) {
        /* create project and save id */
        long projectId = RestHelper.createProject();

        /* post file for project and assert resource was successfully created */
        long fileId = RestHelper.addFileForProjectGetId(projectId, file, false);
        assertNotNull("created resource didn't not return any id or was empty", fileId);
        
        /* get file content */
        String content = expect().
            statusCode(Status.OK.getStatusCode()).log().all().
        when().
            get("/projects/" + projectId + "/files/" + fileId).body().asString();
        LOG.log(Level.INFO, "============== CONTENT ============= {0}", content);
        assertTrue(content.contains("document"));
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testUpdateFileContent(File file) {
        /* create project and save id */
        long projectId = RestHelper.createProject();

        /* add file for project */
        long fileId = RestHelper.addFileForProjectGetId(projectId, file, false);
        assertNotNull("created resource didn't not return any id or was empty", fileId);
        
        long updateFile = System.currentTimeMillis();
        
        /* get file content */
        String content = expect().
            statusCode(Status.OK.getStatusCode()).log().all().
        when().
            get("/projects/"+projectId+"/files/"+fileId).body().asString();
        
        /* update file with same file */
        Response response = given().
            param("content", content).
        expect().
            statusCode(Status.OK.getStatusCode()).log().all().
        when().
            post("/projects/" + projectId + "/files/" + fileId + "/content");

        /* assert updated file has same id */
        long updatedFileId = response.jsonPath().getLong("id");
        assertNotNull("created resource didn't not return any id or was empty", updatedFileId);

        /* updated file modification date must be newer than time before it was changed */
        long updatedFileDate = response.jsonPath().getLong("lastChanged");
        assertTrue("upated file modification date is not newer!", updateFile < updatedFileDate);
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testUpdateFile(File file) {
        long testStart = System.currentTimeMillis();

        /* create project and save id */
        long projectId = RestHelper.createProject();

        /* add file for project */
        long fileId = RestHelper.addFileForProjectGetId(projectId, file, false);
        assertNotNull("created resource didn't not return any id or was empty", fileId);
        
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        HttpServletResponse servletResponse = mock(HttpServletResponse.class);

        /* update file with same file */
        Response response = given().
            multiPart(file).
        expect().
            statusCode(Status.OK.getStatusCode()).log().all().
        when().
            put("/projects/" + projectId + "/files/" + fileId);

        /* assert updated file has same id */
        long updatedFileId = response.jsonPath().getLong("id");
        assertNotNull("created resource didn't not return any id or was empty", updatedFileId);

        /* updated file modification date must be newer than time when the test was started */
        long updatedFileDate = response.jsonPath().getLong("lastChanged");
        assertTrue("upated file modification date is not newer!", testStart < updatedFileDate);
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testUpdateFileInNonExistingProject(File file) {
        given().
            multiPart(file).
        expect().
            statusCode(Status.BAD_REQUEST.getStatusCode()).log().all().
        when().
            put("/projects/12345/files/12345");
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testUpdateNonExistingFile(File file) {
        /* create project and save id */
        long projectId = RestHelper.createProject();

        /**
         * when trying to update a non existing file, expect status code to be
         * CREATED as it will be handled by the server as new resource
         */
        Response response = given().
            multiPart(file).
        expect().
            statusCode(Status.NOT_MODIFIED.getStatusCode()).log().all().
        when().
            put("/projects/" + projectId + "/files/12345");

        /* also, resource including id will be returned */
        long fileId = response.jsonPath().getLong("id");
        assertNotNull("created resource didn't not return any id or was empty", fileId);
    }
    
    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testPatchFileContent(File file) {
        /* create project and save id */
        long projectId = RestHelper.createProject();

        /* add file for project */
        long fileId = RestHelper.addFileForProjectGetId(projectId, file, false);
        assertNotNull("created resource didn't not return any id or was empty", fileId);
        
        long updateFile = System.currentTimeMillis();
        
        /* get file content */
        String content = expect().
            statusCode(Status.OK.getStatusCode()).log().all().
        when().
            get("/projects/"+projectId+"/files/"+fileId).body().asString();
        
        /* mock second string as content update and get patch */
        String changedContent = content.concat("random new String at the End");
        diff_match_patch dmp = new diff_match_patch();
        LinkedList<Diff> diff = dmp.diff_main(content, changedContent);
        LinkedList<Patch> patches = dmp.patch_make(diff);
        String patch = dmp.patch_toText(patches);
        
        /* update file with same file */
        Response response = given().
            param("patch", patch).
        expect().
            statusCode(Status.OK.getStatusCode()).log().all().
        when().
            post("/projects/" + projectId + "/files/" + fileId + "/patch");

        /* assert updated file has same id */
        long updatedFileId = response.jsonPath().getLong("id");
        assertNotNull("created resource didn't not return any id or was empty", updatedFileId);

        /* updated file modification date must be newer than time before it was changed */
        long updatedFileDate = response.jsonPath().getLong("lastChanged");
        assertTrue("upated file modification date is not newer!", updateFile < updatedFileDate);
        
        /* get file content */
        content = expect().
            statusCode(Status.OK.getStatusCode()).log().all().
        when().
            get("/projects/"+projectId+"/files/"+fileId).body().asString();
        assertTrue(content.contains("random new String at the End"));
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "tex-file")
    public void testDeleteFile(File file) {
        /* create project and save id */
        long projectId = RestHelper.createProject();

        /* add file for project */
        long fileId = RestHelper.addFileForProjectGetId(projectId, file, false);
        assertNotNull("created resource didn't not return any id or was empty", fileId);

        /* delete file */
        expect().
            statusCode(Status.NO_CONTENT.getStatusCode()).log().all().
        when().
            delete("/projects/" + projectId + "/files/" + fileId);
    }

    @Test
    public void testDeleteNonExistingFile() {
        /* create project and save id */
        long projectId = RestHelper.createProject();

        /* try to delete non existing file */
        expect().
            statusCode(Status.NO_CONTENT.getStatusCode()).log().all().
        when().
            delete("/projects/" + projectId + "/files/12345");
    }
}
