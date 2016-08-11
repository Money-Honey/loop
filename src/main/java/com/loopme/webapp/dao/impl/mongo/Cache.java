package com.loopme.webapp.dao.impl.mongo;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.loopme.webapp.dao.IRequestLayerDao;
import com.loopme.webapp.dao.impl.controllers.CachingController;
import com.loopme.webapp.model.dto.Advertise;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import javax.inject.Named;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Collection;
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

    @Inject
    private IRequestLayerDao requestLayerDao;

    private CachingController controller = new CachingController();

    @Inject
    public Cache(@Named("isCacheEnabled") Boolean isCacheEnabled,
                 @Named("cacheSize") Integer cacheSize,
                 @Named("expireTimeSecond") Integer expireTimeSecond) {
        controller.setEnabled(isCacheEnabled);

        try {
            MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
            platformMBeanServer.registerMBean(controller, new ObjectName("caching", "name", "controller"));
        } catch (Exception e) {
            Log.error(String.format("Caching controller there was no registered. " +
                    "Use default setting flag. Cache is enabled: '%s'", isCacheEnabled));

            controller.setEnabled(isCacheEnabled);
        }

        cache = CacheBuilder.newBuilder().
                maximumSize(cacheSize).
                expireAfterAccess(expireTimeSecond, TimeUnit.SECONDS).
                removalListener(createRemovalListener()).
                build(createCacheLoader());
    }

    private RemovalListener<Object, Object> createRemovalListener() {
        return notification ->
                Log.debug(String.format("Expired record. Key: '%s'. Value: '%s'.", notification.getKey(), notification.getValue()));
    }

    private CacheLoader<ObjectId, Advertise> createCacheLoader() {
        return new CacheLoader<ObjectId, Advertise>() {
            @Override
            public Advertise load(ObjectId objectId) throws Exception {
                //never used
                return loadAll(Lists.newArrayList(objectId)).get(objectId);
            }

            @Override
            public Map<ObjectId, Advertise> loadAll(Iterable<? extends ObjectId> keys) throws Exception {
                return requestLayerDao.loadRecordsByIdList(Lists.newArrayList(keys));
            }
        };
    }

    Collection<Advertise> loadAll(List<ObjectId> ids) {

        if (!controller.isEnabled()) {
            return requestLayerDao.loadRecordsByIdList(ids).values();
        }

        try {
            return cache.getAll(ids).values();
        } catch (ExecutionException ex) {
            Log.error("Something wrong while fetching data by ids", ex);
            Throwables.propagate(ex);
        }

        return null;
    }
}