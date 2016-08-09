package com.loopme.webapp.generator;

import com.google.common.collect.Lists;
import com.loopme.webapp.dto.Advertise;
import com.mongodb.BasicDBObject;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class AdvertiseGenerator {

    private static final List<String> Countries = Lists.newArrayList(
            "GB", "VA", "UK", "US", "BD", "AL"
    );

    private static final List<String> OperationSystems = Lists.newArrayList(
            "android", "ios", "windows 10 mobile", "blackberry 10", "firefox os", "tizen"
    );

    public List<AdvertiseDbObject> generateRecords(int count) {
        List<AdvertiseDbObject> records = Lists.newArrayList();

        IntStream.range(1, count).forEach(
                id -> records.add(generateRecord(id))
        );

        return records;
    }

    public AdvertiseDbObject generateRecord(int id) {
        AdvertiseDbObject record = new AdvertiseDbObject();

        record.setId(id);
        record.setDescription("funny ads");
        record.setUrl("http://google.com");

        Random rnd = new Random();

        int countiesCount = rnd.nextInt(Countries.size() - 1) + 2;
        int operationSystemsCount = rnd.nextInt(OperationSystems.size() - 1) + 2;

        List<String> countries = Lists.newArrayList();
        IntStream.range(1, countiesCount).forEach(
                idx -> countries.add(Countries.get(rnd.nextInt(simpleAdjust(Countries.size()))))
        );

        List<String> operationSystems = Lists.newArrayList();
        IntStream.range(1, operationSystemsCount).forEach(
                idx -> operationSystems.add(OperationSystems.get(rnd.nextInt(simpleAdjust(OperationSystems.size()))))
        );

        String excludeOs = operationSystems.get(rnd.nextInt(simpleAdjust(operationSystems.size())));
        String excludeCountries = countries.get(rnd.nextInt(simpleAdjust(countries.size())));

        record.setOs(operationSystems);
        record.setCountries(countries);
        record.setExcludedOs(Lists.newArrayList(excludeOs));
        record.setExcludedCountries(Lists.newArrayList(excludeCountries));

        return record;
    }

    private int simpleAdjust(int size) {
        if(size == 0) return 1;
        return size > 1 ? size - 1 : size;
    }
}
