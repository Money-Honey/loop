package com.loopme.webapp.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.loopme.webapp.dao.DbInitializer;

import java.sql.Connection;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class SqlWarmInitializer implements DbInitializer {

    @Inject
    private Provider<Connection> connectionProvider;

    @Override
    public void warmDb() {

    }
}
