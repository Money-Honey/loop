package com.loopme.webapp.generator;

import com.loopme.webapp.model.AdvertiseDbObject;
import org.junit.Test;

import java.util.List;

import static com.loopme.webapp.generator.AdvertiseGenerator.generateRecord;
import static com.loopme.webapp.generator.AdvertiseGenerator.generateRecords;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class AdvertiseGeneratorTest {

    @Test
    public void generateRecordShouldReturnNoNullableRecordWithAtLeastOneCountryOs() {
        AdvertiseDbObject advertiseDbObject = generateRecord(1);

        assertNotNull(advertiseDbObject);
        assertEquals(advertiseDbObject.getId(), 1);
        assertTrue(advertiseDbObject.getCountries().size() >= 1);
        assertTrue(advertiseDbObject.getOs().size() >= 1);
        assertTrue(advertiseDbObject.getExcludedCountries().size() >= 1);
        assertTrue(advertiseDbObject.getExcludedOs().size() >= 1);
    }

    @Test
    public void generateRecordsShouldReturnTheSameSizeArray() {
        List<AdvertiseDbObject> ads = generateRecords(100);

        assertNotNull(ads);
        assertThat(ads, hasSize(100));
    }
}