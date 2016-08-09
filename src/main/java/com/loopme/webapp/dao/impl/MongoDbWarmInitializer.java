package com.loopme.webapp.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.loopme.webapp.dao.DbInitializer;
import com.loopme.webapp.generator.AdvertiseGenerator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class MongoDbWarmInitializer implements DbInitializer {

    static Logger Log = Logger.getLogger(MongoDbWarmInitializer.class.getName());

    @Inject
    private Provider<DBCollection> collectionProvider;

    @Inject
    private AdvertiseGenerator generator;

    @Override
    public void warmDb() {
        DBCollection dbCollection = collectionProvider.get();

        List<BasicDBObject> records = generator.generateRecords(5);
        dbCollection.insert(records);

        Log.info("Db warmed. Records: " + records);
    }
}
