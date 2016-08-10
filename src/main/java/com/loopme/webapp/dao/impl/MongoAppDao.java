package com.loopme.webapp.dao.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.loopme.webapp.dao.AppDao;
import com.loopme.webapp.dto.Advertise;
import com.loopme.webapp.dto.AdvertiseRequestEvent;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.log4j.Logger;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;

//import static com.mongodb.client.model.Aggregates.match;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class MongoAppDao implements AppDao {

    static Logger Log = Logger.getLogger(MongoAppDao.class.getName());

    @Inject
    private Provider<DBCollection> connectionProvider;

    @Override
    public List<Advertise> retrieveAdvertises(AdvertiseRequestEvent event) {
        Log.info("Request: " + event);

        List<Advertise> records = Lists.newArrayList();

        DBCollection collection = connectionProvider.get();

        BasicDBObject query = createQuery(event);
        Log.info("Querry: " + query.toString());

//        try (DBCursor cursor = collection.find(query).limit(event.getLimit())) {
//            while (cursor.hasNext()) records.add(new Advertise().fromJson((BasicDBObject)cursor.next()));
//        }

        collection.aggregate(createAggregateQuery(event)).results().forEach(
                json ->
                        records.add(new Advertise().fromJson((BasicDBObject) json))
        );

        // Get one random document from the mycoll collection.
//        db.mycoll.aggregate(
//                { $sample: { size: 1 } }
//        )

        Log.info("Return: " + records);

        return records;
    }

    private List<Bson> createAggregateQuery(AdvertiseRequestEvent event) {
        return Arrays.asList(
                match(
                        and(
                                and(eq("countries", event.getCountry()), eq("os", event.getOs())),
                                or(ne("excluded_countries", event.getCountry()), ne("excluded_os", event.getOs())))
                        ),
                sample(event.getLimit()));
    }

    private List<? extends DBObject> createQueryForAggregateFunc(AdvertiseRequestEvent event) {
        List<BasicDBObject> aggregation = Lists.newArrayList();

        aggregation.add(new BasicDBObject("$sample", new BasicDBObject("size", event.getLimit())));
        aggregation.add(new BasicDBObject("$match", createQuery(event)));

        Log.info(aggregation);

        return aggregation;
    }

    private BasicDBObject createQuery(AdvertiseRequestEvent event) {

        /*
        * { "$and" : [ { "countries" : "BD" , "os" : "windows 10 mobile"} , { "$or" : [ {"excluded_countries" : { "$ne" : "UK" } } , { "excluded_os" : { "$ne" : "windows 10 mobile" } } ] } ]}
        * */

        BasicDBObject queryFind = new BasicDBObject();
        queryFind.put("countries", event.getCountry());
        queryFind.put("os", event.getOs());

        BasicDBObject exclusiveQuery = new BasicDBObject();

        List<BasicDBObject> excludes = Lists.newArrayList();

        excludes.add(new BasicDBObject("excluded_countries", new BasicDBObject("$ne", event.getCountry())));
        excludes.add(new BasicDBObject("excluded_os", new BasicDBObject("$ne", event.getOs())));

        exclusiveQuery.put("$or", excludes);

        BasicDBObject andQuery = new BasicDBObject();
        List<BasicDBObject> obj = Lists.newArrayList();
        obj.add(queryFind);
        obj.add(exclusiveQuery);
        andQuery.put("$and", obj);


        return andQuery;
    }
}