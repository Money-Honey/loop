package com.loopme.webapp.dao.impl.mongo;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.loopme.webapp.dao.AppDao;
import com.loopme.webapp.dto.Advertise;
import com.loopme.webapp.dto.AdvertiseRequestEvent;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.List;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class MongoAppDao implements AppDao {

    static Logger Log = Logger.getLogger(MongoAppDao.class.getName());

    @Inject
    private Cache cache;

    @Inject
    private RequestLayerDao requestLayerDao;

    @Override
    public List<Advertise> retrieveAdvertises(AdvertiseRequestEvent event) {
        Log.info("Request: " + event);

        List<ObjectId> idList = requestLayerDao.loadMatchedIds(event);
        Log.info("Id list: " + idList);

        Collections.shuffle(idList);

        idList = trimToLimit(event.getLimit(), idList);

        if(idList.isEmpty()) {
            return Lists.newArrayList();
        } else {
            return Lists.newArrayList(cache.loadAll(idList));
        }
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
}