/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniluebeck.compilatex.test;

import com.jayway.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class TestBase {
    
    @BeforeClass
    public void setUpClass() throws Exception {
        RestAssured.port = 9090; // 8888;
        RestAssured.basePath = "/rest/"; // "/compilatex/rest/";
    }
}
