package com.loopme.webapp;

import com.loopme.webapp.model.AdvertiseDbObject;
import com.loopme.webapp.model.dto.AdvertiseRequestEvent;
import com.mongodb.BasicDBObject;
import org.junit.After;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static com.loopme.webapp.generator.AdvertiseGenerator.generateRecord;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class IntegrationRestTest extends BaseRestTest {

    @After
    public void afterTestCleanup() {
        fongoDb.getCollection(dbCollectionName).remove(new BasicDBObject());
    }

    @Test
    public void requestPostToAdsUrlShouldReturnJson() {
        AdvertiseDbObject testDbRecord = generateRecordWithoutExclusions(1, "windows 10 mobile", "US");

        insertDbRecord(testDbRecord);

        AdvertiseRequestEvent msg = new AdvertiseRequestEvent("US", "windows 10 mobile", 1);

        given().
                contentType("application/json").
                body(msg).
        when().
                post("/ads").
        then().
                body("ads", hasSize(1));
    }

    @Test
    public void badRequestShouldReturnStatus500() {
        given().
                contentType("application/json").
                body("Something other. Not event.").
        when().
                post("/ads").
        then().
                statusCode(500);
    }

    private AdvertiseDbObject generateRecordWithoutExclusions(int id, String os, String country) {
        AdvertiseDbObject record = generateRecord(id, os, country);

        record.getExcludedCountries().clear();
        record.getExcludedCountries().add("NOT " + country);
        record.getExcludedOs().clear();
        record.getExcludedOs().add("NOT " + os);

        return record;
    }
}