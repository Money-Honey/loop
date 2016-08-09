package com.loopme.webapp;

import com.loopme.webapp.dto.AdvertiseRequestEvent;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class RequestTest extends BaseTest {

    @Test
    public void requestPostToHoleSlashPostShouldReturnJson() {
        AdvertiseRequestEvent msg = new AdvertiseRequestEvent("US", "ios", 1);
        given().
                contentType("application/json").
                body(msg).
        when().
                post("/hole").
        then().
                body("ads", hasSize(1));
    }
}
