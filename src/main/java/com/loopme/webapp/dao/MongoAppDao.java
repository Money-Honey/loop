package com.loopme.webapp.dao;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.loopme.webapp.dto.Advertise;
import com.loopme.webapp.dto.AdvertiseRequestEvent;
import com.mongodb.FongoDBCollection;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class MongoAppDao implements AppDao {

    static Logger Log = Logger.getLogger(MongoAppDao.class.getName());

    @Inject
    private Provider<FongoDBCollection> connectionProvider;

    @Override
    public List<Advertise> retrieveAdvertises(AdvertiseRequestEvent event) {

        List<Advertise> ads = Lists.newArrayList();

        FongoDBCollection collection = connectionProvider.get();



        return ads;
    }
}
