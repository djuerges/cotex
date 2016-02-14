/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.collaboratex.test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.jayway.restassured.RestAssured;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class TestBase {

    /* instantiate datastore every time before a test class is run (GAE design prevents @beforesuite)*/
    private final LocalServiceTestHelper datastoreHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @BeforeClass
    protected void setUp() {
//        RestAssured.port = 8080;
        RestAssured.basePath = "/rest/";
        datastoreHelper.setUp();
    }

    @AfterClass
    protected void tearDown() {
        datastoreHelper.tearDown();
    }
}
