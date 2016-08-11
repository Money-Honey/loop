package com.loopme.webapp.dao.impl.mongo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.loopme.webapp.dao.AppDao;
import com.loopme.webapp.model.dto.Advertise;
import com.loopme.webapp.model.dto.AdvertiseRequestEvent;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class MongoAppDao implements AppDao {

    static Logger Log = Logger.getLogger(MongoAppDao.class.getName());

    @Inject
    private Provider<DBCollection> connectionProvider;

    public Map<ObjectId, Advertise> loadRecordsByIdList(List<ObjectId> idList) {

        DBObject recordsByIdsQuery = createQueryRecordsByIds(idList);

        Map<ObjectId, Advertise> records = Maps.newHashMap();
        try (DBCursor cursor = connectionProvider.get().find(recordsByIdsQuery)) {
            int idx = 0;
            while (cursor.hasNext() && idx < idList.size()) {

                records.put(idList.get(idx), new Advertise().fromJson((BasicDBObject) cursor.next()));

                idx++;
            }
        }
        return records;
    }

    public List<ObjectId> loadMatchedIds(AdvertiseRequestEvent event) {
        DBObject query = createQuery(event);

        List<ObjectId> result = Lists.newArrayList();
        try (DBCursor cursor = connectionProvider.get().find(query, new BasicDBObject())) {
            while (cursor.hasNext()) result.add(new ObjectId(((BasicDBObject) cursor.next()).getString("_id")));
        }
        return result;
    }

    private BasicDBObject createQueryRecordsByIds(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject("_id", new BasicDBObject("$in", ids));

        Log.debug("QuerryByIds: " + query.toString());

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

        Log.debug("Querry forId: " + andQuery.toString());

        return andQuery;
    }
}