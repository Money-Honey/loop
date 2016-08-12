package com.loopme.webapp;

import com.github.fakemongo.Fongo;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.util.Modules;
import com.loopme.webapp.model.AdvertiseDbObject;
import com.loopme.webapp.model.dto.Advertise;
import com.loopme.webapp.model.dto.AdvertiseRequestEvent;
import com.loopme.webapp.modules.CachModule;
import com.loopme.webapp.modules.MongoDataBaseModule;
import com.loopme.webapp.services.AppService;
import com.loopme.webapp.services.AppServiceImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static com.loopme.webapp.generator.AdvertiseGenerator.generateRecord;
import static com.loopme.webapp.generator.AdvertiseGenerator.generateRecords;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class JsonCreateTest {

    static Provider<DBCollection> collectionProvider;

    static AppService service;

    @BeforeClass
    public static void init() {
        Fongo fongo = new Fongo("mongo fake server");
        Injector injector = Guice.createInjector(Modules.override(new MongoDataBaseModule(fongo.getDB("fongoname"), "ads"), new CachModule(true, 10, 1))
                .with(binder -> {
                    binder.bind(AppService.class).toInstance(new AppServiceImpl());
                }));

        service = injector.getInstance(AppService.class);
        collectionProvider = injector.getProvider(DBCollection.class);
    }

    @Before
    public void cleanUp(){
        collectionProvider.get().remove(new BasicDBObject());
    }

    @Test
    public void insertOneRecordAndRetriveItFromCollectionWithoutExclusions() {
        AdvertiseDbObject record = generateRecord(1, "ios", "UA");

        record.getExcludedCountries().clear();
        record.getExcludedOs().clear();

        AdvertiseRequestEvent event = new AdvertiseRequestEvent("UA", "ios", 4);

        showDetails(record, event);

        collectionProvider.get().insert(record.toJson());
        List<Advertise> advertises = service.proposeAdvertises(event);

        assertThat(advertises, hasSize(1));
    }

    @Test
    public void insertOneRecordAndRetriveItFromCollectionWithExclusionsShoudBeFiltered() {
        String os = "ios";
        String country = "UA";
        AdvertiseDbObject record = generateRecord(1, os, country);

        record.getExcludedOs().add(os);
        record.getExcludedCountries().add(country);

        AdvertiseRequestEvent event = new AdvertiseRequestEvent(country, os, 4);

        showDetails(record, event);

        collectionProvider.get().insert(record.toJson());
        List<Advertise> advertises = service.proposeAdvertises(event);

        assertThat(advertises, hasSize(0));
    }

    @Test
    public void insertOneRecordAndRetriveItFromCollectionWithOneShoudNotBeFiltered() {
        String os = "ios";
        String country = "UA";
        AdvertiseDbObject record = generateRecord(1, os, country);

        record.getExcludedOs().add(os);
        record.getExcludedCountries().clear();

        AdvertiseRequestEvent event = new AdvertiseRequestEvent(country, os, 4);

        showDetails(record, event);

        collectionProvider.get().insert(record.toJson());
        List<Advertise> advertises = service.proposeAdvertises(event);

        assertThat(advertises, hasSize(1));
    }

    @Test
    public void insertRecordsWithoutExclusionWithCommonOsCountryThenTheResultShouldBeLimited() {
        int total = 3;
        int limit = total - 1;
        List<AdvertiseDbObject> records = generateRecords(total);

        String testCountry = "Country";
        String testOs = "Os";

        records.forEach(record -> {
            record.getCountries().add(testCountry);
            record.getOs().add(testOs);
            record.getExcludedCountries().clear();
            record.getExcludedOs().clear();
        });

        AdvertiseRequestEvent event = new AdvertiseRequestEvent(testCountry, testOs, limit);

        records.forEach(record ->
                        collectionProvider.get().insert(record.toJson())
        );

        List<Advertise> advertises = service.proposeAdvertises(event);

        assertThat(advertises, hasSize(limit));
    }

    private void showDetails(AdvertiseDbObject record, AdvertiseRequestEvent event) {
        log("To insert: " + record);
        log("To insert Json: " + record.toJson());
        log("Send requestEvent: " + event);
    }

    public static void log(Object msg) {
        System.out.println(msg);
    }
}