package com.loopme.webapp.dao.impl.mongo;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.loopme.webapp.dto.Advertise;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class Cache {

    static Logger Log = Logger.getLogger(Cache.class.getName());

    final LoadingCache<ObjectId, Advertise> cache;

    final Boolean isCacheEnabled;

    @Inject
    public Cache(@Named("isCacheEnabled") Boolean isCacheEnabled) {
        this.isCacheEnabled = isCacheEnabled;

        cache = CacheBuilder.<ObjectId, Advertise>newBuilder().
                maximumSize(2000).
                expireAfterAccess(10, TimeUnit.SECONDS).
                removalListener(
                        notification ->
                                Log.debug(String.format("Expired record. Key: '%s'. Value: '%s'.", notification.getKey(), notification.getValue()))
                ).
                build(new CacheLoader<ObjectId, Advertise>() {
                    @Override
                    public Advertise load(ObjectId objectId) throws Exception {
                        //never used
                        return null;
                    }

                    @Override
                    public Map<ObjectId, Advertise> loadAll(Iterable<? extends ObjectId> keys) throws Exception {
                        //the main entry point
                        return null;
                    }
                });
    }

    Map<ObjectId, Advertise> loadAll(List<ObjectId> ids) {

        if (!isCacheEnabled) {
            return Maps.newHashMap();
        }

        try {
            return cache.getAll(ids);
        } catch (ExecutionException ex) {
            Log.error("Something wrong while fetching data by ids", ex);
            Throwables.propagate(ex);
        }

        return null;
    }

    void put(List<ObjectId> ids, List<Advertise> ads) {
        if (!isCacheEnabled) {
            return;
        }

        Preconditions.checkArgument(ids.size() == ads.size());

        Map<ObjectId, Advertise> records = Maps.newHashMap();

        for (int i = 0; i < ids.size(); i++) {
            records.putIfAbsent(ids.get(i), ads.get(i));
        }

        cache.putAll(records);
    }
}