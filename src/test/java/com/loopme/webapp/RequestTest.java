package com.loopme.webapp;

import com.loopme.webapp.dto.AdvertiseRequestEvent;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class RequestTest extends BaseTest {

    @Test
    public void requestGetToHoleShouldReturnHappanedString() {
        String respond = get("/hole").asString();
        assertThat(respond, is("happened"));
    }

    @Test
    public void requestPostToHoleSlashPostShouldReturnJson() {
        AdvertiseRequestEvent msg = new AdvertiseRequestEvent("UK", "ios", 4);
        given().
                contentType("application/json").
                body(msg).
        when().
                post("/hole/post").
        then().
                body("ads", hasSize(1)).
                time(lessThan(1L), TimeUnit.SECONDS);
    }
}
