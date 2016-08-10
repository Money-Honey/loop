package com.loopme.webapp.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.loopme.webapp.dao.AppDao;
import com.loopme.webapp.dao.DbInitializer;
import com.loopme.webapp.dao.impl.mongo.MongoAppDao;
import com.loopme.webapp.dao.impl.mongo.MongoDbWarmInitializer;
import com.loopme.webapp.generator.AdvertiseGenerator;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class MongoDataBaseModule implements Module {

    private final DB db;

    public MongoDataBaseModule(DB db) {
        super();
        this.db = db;
    }

    @Override
    public void configure(Binder binder) {

        binder.bind(AppDao.class).toInstance(new MongoAppDao());
        binder.bind(AdvertiseGenerator.class).in(Scopes.SINGLETON);
        binder.bind(DbInitializer.class).toInstance(new MongoDbWarmInitializer());
        binder.bind(DBCollection.class).toProvider(() -> db.getCollection("abs"));
    }
}
