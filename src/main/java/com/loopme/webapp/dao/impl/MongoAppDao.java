package com.loopme.webapp.dao.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.loopme.webapp.dao.AppDao;
import com.loopme.webapp.dto.Advertise;
import com.loopme.webapp.dto.AdvertiseRequestEvent;
import com.mongodb.*;
import org.apache.log4j.Logger;

import java.util.List;

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

        BasicDBObject query = createComplexQuery(event);
//        BasicDBObject query = createComplex2Query(event);
        Log.info("Querry: " + query.toString());

        try (DBCursor cursor = collection.find(query).limit(event.getLimit())) {
            while (cursor.hasNext()) records.add(new Advertise().fromJson((BasicDBObject)cursor.next()));
        }

        Log.info("Return: " + records);

        return records;
    }

    private BasicDBObject createComplexQuery(AdvertiseRequestEvent event) {

        BasicDBObject queryFind = new BasicDBObject();
        queryFind.put("countries", event.getCountry());
        queryFind.put("os", event.getOs());

        BasicDBObject exclusiveQuery = new BasicDBObject();
        exclusiveQuery.put("excluded_countries", new BasicDBObject("$ne", event.getCountry()));
        exclusiveQuery.put("excluded_os", new BasicDBObject("$ne", event.getOs()));

        BasicDBObject andQuery = new BasicDBObject();
        List<BasicDBObject> obj = Lists.newArrayList();
        obj.add(queryFind);
        obj.add(exclusiveQuery);
        andQuery.put("$and", obj);

        return andQuery;
    }

    private BasicDBObject createComplex2Query(AdvertiseRequestEvent event) {

        BasicDBObject queryFind = new BasicDBObject();
        queryFind.put("countries", event.getCountry());
        queryFind.put("os", event.getOs());

        BasicDBObject exclusiveQuery = new BasicDBObject();
        BasicDBList orCountry = new BasicDBList();
        orCountry.put("$exists", "false");
        orCountry.put("$ne", event.getCountry());

        exclusiveQuery.put("$or", orCountry);

        BasicDBList orOs = new BasicDBList();
        orCountry.put("$exists", "false");
        orCountry.put("$ne", event.getOs());

        exclusiveQuery.put("$or", orOs);


//        exclusiveQuery.put("excluded_os", new BasicDBObject("$ne", event.getOs()));

        BasicDBObject andQuery = new BasicDBObject();
        List<BasicDBObject> obj = Lists.newArrayList();
        obj.add(queryFind);
        obj.add(exclusiveQuery);
        andQuery.put("$and", obj);

        return andQuery;
    }
}