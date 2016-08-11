package com.loopme.webapp;

import com.google.common.collect.Lists;
import com.loopme.webapp.generator.AdvertiseGenerator;
import com.loopme.webapp.model.AdvertiseDbObject;
import com.loopme.webapp.model.dto.AdvertiseRequestEvent;
import com.mongodb.BasicDBObject;
import org.junit.After;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static com.loopme.webapp.generator.AdvertiseGenerator.generateRecord;
import static org.hamcrest.Matchers.hasSize;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class RequestTest extends BaseTest {

    @After
    public void afterTestCleanup() {
        fongoDb.getCollection(dbCollectionName).remove(new BasicDBObject());
    }

    @Test
    public void requestPostToHoleSlashPostShouldReturnJson() {
        AdvertiseDbObject testDbRecord = generateRecord(1);
        testDbRecord.setCountries(Lists.newArrayList("US"));
        testDbRecord.setOs(Lists.newArrayList("windows 10 mobile"));

        testDbRecord.setExcludedCountries(Lists.newArrayList("NOT US"));
        testDbRecord.setExcludedOs(Lists.newArrayList("NOT windows 10 mobile"));

        insert(testDbRecord);

        AdvertiseRequestEvent msg = new AdvertiseRequestEvent("US", "windows 10 mobile", 1);

        given().
                contentType("application/json").
                body(msg).
        when().
                post("/hole").
        then().
                body("ads", hasSize(1));
    }

    @Test
    public void badRequestShouldReturnStatus500() {
        given().
                contentType("application/json").
                body("Something other. Not event.").
        when().
                post("/hole").
        then().
                statusCode(500);
    }
}