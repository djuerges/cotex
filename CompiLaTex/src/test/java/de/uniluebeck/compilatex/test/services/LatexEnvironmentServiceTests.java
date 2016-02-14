package de.uniluebeck.compilatex.test.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import static com.jayway.restassured.RestAssured.expect;
import com.jayway.restassured.response.Response;
import de.uniluebeck.compilatex.test.TestBase;
import java.util.logging.Logger;
import javax.ws.rs.core.Response.Status;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class LatexEnvironmentServiceTests extends TestBase {

    public static final Logger LOG = Logger.getLogger(LatexEnvironmentServiceTests.class.getName());
    
    @Test
    public void testGetLatexEnvironments() {
        Response response = expect().
            statusCode(Status.OK.getStatusCode()).log().all().
        when().
            get("/latex/environments");
        
//        /* assert returned values are valid environments */
//        List<String> values = response.jsonPath().getList("value");
//        for(String environment : values){
//            assertNotNull("not a valid latex environment!", LatexEnvironment.valueOfName(environment));
//        }
    }
}
