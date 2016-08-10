package com.loopme.webapp.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.loopme.webapp.dao.DbInitializer;
import com.loopme.webapp.generator.AdvertiseDbObject;
import com.loopme.webapp.generator.AdvertiseGenerator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

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

        dbCollection.createIndex(createCompoundIndexKeys());

        List<AdvertiseDbObject> records = generator.generateRecords(2);
        List<BasicDBObject> dbObjects = records.stream()
                .map(record -> record.toJson())
                .collect(Collectors.toList());

        dbCollection.insert(dbObjects);

        Log.info("Db warmed. Records: " + dbObjects);
    }

    private BasicDBObject createCompoundIndexKeys() {
        BasicDBObject compoundIndex = new BasicDBObject();
        compoundIndex.put("countries", 1);
        compoundIndex.put("os", -1);
        return compoundIndex;
    }
}
