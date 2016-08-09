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

        List<Advertise> ads = Lists.newArrayList();

        DBCollection collection = connectionProvider.get();

        BasicDBObject query = new BasicDBObject();
        List<BasicDBObject> queryObj = Lists.newArrayList();
        queryObj.add(new BasicDBObject("os", event.getOs()));
        queryObj.add(new BasicDBObject("country", event.getCountry()));
        query.put("$and", queryObj);


        DBCursor cursor = collection.find(query);
        while (cursor.hasNext()) {
            ads.add((Advertise)cursor.next());
        }

        return ads;
    }
}
