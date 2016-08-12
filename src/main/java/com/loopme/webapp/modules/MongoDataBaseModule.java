package com.loopme.webapp.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.loopme.webapp.dao.AppDao;
import com.loopme.webapp.dao.DbInitializer;
import com.loopme.webapp.dao.impl.CacheableDaoCoordinator;
import com.loopme.webapp.dao.impl.mongo.MongoAppDao;
import com.loopme.webapp.dao.impl.mongo.MongoDbWarmInitializer;
import com.loopme.webapp.generator.AdvertiseGenerator;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class MongoDataBaseModule implements Module {

    private final DB db;
    private final String collectionName;

    public MongoDataBaseModule(DB db, String collectionName) {
        super();
        this.db = db;
        this.collectionName = collectionName;
    }

    @Override
    public void configure(Binder binder) {

        binder.bind(AppDao.class).toInstance(new MongoAppDao());
        binder.bind(AdvertiseGenerator.class).in(Scopes.SINGLETON);
        binder.bind(CacheableDaoCoordinator.class).in(Scopes.SINGLETON);
        binder.bind(DbInitializer.class).toInstance(new MongoDbWarmInitializer());
        binder.bind(DBCollection.class).toProvider(() -> db.getCollection(collectionName));
    }
}
