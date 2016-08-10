package com.loopme.webapp.dao.impl.mongo;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class MongoAppDao implements AppDao {

    static Logger Log = Logger.getLogger(MongoAppDao.class.getName());

    @Inject
    private Provider<DBCollection> connectionProvider;

    @Inject
    private Cache cache;

    @Override
    public List<Advertise> retrieveAdvertises(AdvertiseRequestEvent event) {
        Log.info("Request: " + event);

        DBCollection collection = connectionProvider.get();


        List<ObjectId> idList = loadMatchedIds(collection, createQuery(event));
        Log.info("Id list: " + idList);

        Collections.shuffle(idList, new Random(System.nanoTime()));
        idList = trimToLimit(event.getLimit(), idList);

        if(idList.isEmpty()) {
            return Lists.newArrayList();
        } else {
            Map<ObjectId, Advertise> cachRecords = cache.load(idList);

            if(cachRecords.size() == idList.size()) {
                return Lists.newArrayList(cachRecords.values());
            } else {

                idList.removeAll(cachRecords.keySet());

                List<Advertise> dbRecords = loadRecordsByIdList(collection, createQueryRecordsByIds(idList));

                cache.put(idList, dbRecords);

                dbRecords.addAll(cachRecords.values());
                return dbRecords;
            }
        }
    }

    private List<Advertise> loadRecordsByIdList(DBCollection collection, DBObject recordsByIdsQuery) {
        List<Advertise> records = Lists.newArrayList();
        try (DBCursor cursor = collection.find(recordsByIdsQuery)) {
            while (cursor.hasNext()) records.add(new Advertise().fromJson((BasicDBObject) cursor.next()));
        }
        return records;
    }

    private List<ObjectId> trimToLimit(int limit, List<ObjectId> idList) {
        Preconditions.checkArgument(limit > 0, "Limit couldn't be zero");

        if(idList.size() == 0 || idList.size() == 1) return idList;

        int listSize = idList.size();

        int subListSize = limit;
        if (subListSize > listSize) {
            subListSize = listSize;
        }

        return idList.subList(0, subListSize);
    }

    private List<ObjectId> loadMatchedIds(DBCollection collection, DBObject query) {
        List<ObjectId> result = Lists.newArrayList();
        try (DBCursor cursor = collection.find(query, new BasicDBObject())) {
            while (cursor.hasNext()) result.add(new ObjectId(((BasicDBObject) cursor.next()).getString("_id")));
        }
        return result;
    }

    private BasicDBObject createQueryRecordsByIds(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject("_id", new BasicDBObject("$in", ids));

        Log.info("QuerryByIds: " + query.toString());

        return query;
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

        Log.info("Querry forId: " + andQuery.toString());

        return andQuery;
    }
}