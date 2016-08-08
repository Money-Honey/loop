package com.loopme.webapp.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.loopme.webapp.dao.AppDao;
import com.loopme.webapp.dao.MongoAppDao;
import com.mongodb.FongoDB;
import com.mongodb.FongoDBCollection;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class MongoDataBaseModule implements Module {

    private final FongoDB db;

    public MongoDataBaseModule(FongoDB db) {
        super();
        this.db = db;
    }

    @Override
    public void configure(Binder binder) {

        binder.bind(AppDao.class).toInstance(new MongoAppDao());
        binder.bind(FongoDBCollection.class).toProvider(() -> db.getCollection("abs"));
    }
}
