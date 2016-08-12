package com.loopme.webapp;

import com.github.fakemongo.Fongo;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.google.inject.util.Modules;
import com.jayway.restassured.RestAssured;
import com.loopme.webapp.dao.DbInitializer;
import com.loopme.webapp.model.AdvertiseDbObject;
import com.loopme.webapp.modules.MongoDataBaseModule;
import com.loopme.webapp.runnable.EntryPoint;
import com.mongodb.FongoDB;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.Properties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class BaseRestTest {
    static String dbCollectionName = "mycollectionName";

    static protected FongoDB fongoDb;

    static private EntryPoint serverPoint;

    @BeforeClass
    public static void configure() throws Exception {
        String port = "8081";
        RestAssured.port = Integer.valueOf(port);

        Properties propertiesMock = mock(Properties.class);
        when(propertiesMock.getProperty(EntryPoint.SERVER_PORT)).thenReturn(port);
        when(propertiesMock.getProperty(EntryPoint.CACHE_USE)).thenReturn("false");
        when(propertiesMock.getProperty(EntryPoint.CACHE_EXPIRE_SECONDS)).thenReturn("10");

        serverPoint = new EntryPoint(propertiesMock) {
            @Override
            protected Module createDataBaseModule() {
                Fongo fongo = new Fongo("mongo fake server");
                fongoDb = fongo.getDB("loopme");

                return Modules.override(new MongoDataBaseModule(fongoDb, dbCollectionName)).with(binder -> {
                    binder.bind(Integer.class).annotatedWith(Names.named("initialRecordsCount")).toInstance(0);
                });
            }
        };

        serverPoint.configureAndStart(true);
    }

    @AfterClass
    public static void cleanup() throws Exception {
        serverPoint.stopServer();
    }

    protected void insertDbRecord(AdvertiseDbObject dbrecord) {
        fongoDb.getCollection(dbCollectionName).insert(dbrecord.toJson());
    }
}
