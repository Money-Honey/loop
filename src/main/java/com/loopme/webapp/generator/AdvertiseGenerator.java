package com.loopme.webapp.generator;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.loopme.webapp.model.AdvertiseDbObject;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class AdvertiseGenerator {

    private static final List<String> Countries = Lists.newArrayList(
            "GB", "VA", "UK", "US", "BD", "AL"
    );

    private static final List<String> OperationSystems = Lists.newArrayList(
            "android", "ios", "windows 10 mobile", "blackberry 10", "firefox os", "tizen"
    );

    public static List<AdvertiseDbObject> generateRecords(int count) {
        List<AdvertiseDbObject> records = Lists.newArrayList();

        IntStream.range(1, count + 1).forEach(
                id -> records.add(generateRecord(id))
        );

        return records;
    }

    public static AdvertiseDbObject generateRecord(int id, String os, String country) {
        AdvertiseDbObject record = generateRecord(id);

        record.getCountries().add(country);
        record.getOs().add(os);

        return record;
    }

    public static AdvertiseDbObject generateRecord(int id) {
        AdvertiseDbObject record = new AdvertiseDbObject();

        record.setId(id);
        record.setDescription("funny ads");
        record.setUrl("http://google.com");

        Random rnd = new Random(System.nanoTime());

        int countiesCount = rnd.nextInt(Countries.size() - 1) + 2;
        int operationSystemsCount = rnd.nextInt(OperationSystems.size() - 1) + 2;

        Set<String> countries = Sets.newHashSet();
        IntStream.range(1, countiesCount).forEach(
                idx -> countries.add(Countries.get(rnd.nextInt(adjustForArrayBounds(Countries.size()))))
        );

        Set<String> operationSystems = Sets.newHashSet();
        IntStream.range(1, operationSystemsCount).forEach(
                idx -> operationSystems.add(OperationSystems.get(rnd.nextInt(adjustForArrayBounds(OperationSystems.size()))))
        );

        String excludeOs = operationSystems.iterator().next();
        String excludeCountries = countries.iterator().next();

        record.setOs(operationSystems);
        record.setCountries(countries);
        record.setExcludedOs(Sets.newHashSet(excludeOs));
        record.setExcludedCountries(Sets.newHashSet(excludeCountries));

        return record;
    }

    private static int adjustForArrayBounds(int size) {
        if(size == 0 || size == 1) return 1;
        if(size > 1) {
            return size - 1;
        } else {
            return size;
        }
    }
}
