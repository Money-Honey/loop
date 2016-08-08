package com.loopme.webapp;

import com.jayway.restassured.RestAssured;
import org.junit.BeforeClass;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class BaseTest {
    @BeforeClass
    public static void configureRestAssured() throws Exception
    {
        RestAssured.port = 8081;
//        RestAssured.basePath = "/";
    }
}
