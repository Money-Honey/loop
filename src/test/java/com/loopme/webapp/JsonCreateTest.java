package com.loopme.webapp;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class JsonCreateTest {

    @Test
    public void basicObjectShouldRepresentJsonCorrectly() {
        assertThat(new BasicDBObject("key", "value").toJson(), is("{ \"key\" : \"value\" }"));
    }

    @Test
    public void basicComplexObjectShouldRepresentJsonCorrectly() {
        BasicDBObject record = new BasicDBObject()
                .append("id", 1)
                .append("description", "funny ads")
                .append("url", "http://google.com");

        record.put("os", new String[]{"ios", "android"});
        record.put("countries", new String[]{"UK", "US"});
        record.put("excluded_os", new String[]{"ios"});
        record.put("excluded_countries", new String[]{"UK"});

        System.out.println(record.toString());

        assertTrue(true);
    }

    @Test
    public void createQuerry() {
        BasicDBObject query = new BasicDBObject();
        List<BasicDBObject> queryObj = Lists.newArrayList();
        queryObj.add(new BasicDBObject("os", "ios"));
        queryObj.add(new BasicDBObject("country", "UK"));
        query.put("$and", queryObj);

        System.out.println(query);
    }
}
