package com.loopme.webapp.dao.impl.mongo;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.loopme.webapp.dao.DbInitializer;
import com.loopme.webapp.model.AdvertiseDbObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

import static com.loopme.webapp.generator.AdvertiseGenerator.generateRecords;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class MongoDbWarmInitializer implements DbInitializer {

    static Logger Log = Logger.getLogger(MongoDbWarmInitializer.class.getName());

    @Inject
    private Provider<DBCollection> collectionProvider;

    @Inject(optional = true)
    @Named("initialRecordsCount")
    private Integer initialRecordsCount = 2000;

    @Override
    public void warmDb() {
        DBCollection dbCollection = collectionProvider.get();

        dbCollection.createIndex(createCompoundIndexKeys());

        List<AdvertiseDbObject> records = generateRecords(initialRecordsCount);
        List<BasicDBObject> dbObjects = records.stream()
                .map(record -> record.toJson())
                .collect(Collectors.toList());

        dbCollection.insert(dbObjects);

        showInfo(dbObjects);
    }

    private void showInfo(List<BasicDBObject> dbObjects) {
        Log.info("Db warmed. Records size: " + dbObjects.size());
        if(!dbObjects.isEmpty()) {
            Log.info("First 10 records: ");

            for (int i = 0; i < dbObjects.size(); i++) {
                if (i >= 10) break;

                Log.info(dbObjects.get(i));
            }
        }
    }

    private BasicDBObject createCompoundIndexKeys() {
        BasicDBObject compoundIndex = new BasicDBObject();

        compoundIndex.put("countries", 1);
        compoundIndex.put("os", 1);

        return compoundIndex;
    }
}
