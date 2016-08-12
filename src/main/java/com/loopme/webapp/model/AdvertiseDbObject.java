package com.loopme.webapp.model;

import com.mongodb.BasicDBObject;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
@Data
public class AdvertiseDbObject implements MongoBean<AdvertiseDbObject> {
    int id;
    String description;
    String url;
    Set<String> os;
    Set<String> countries;
    Set<String> excludedOs;
    Set<String> excludedCountries;

    @Override
    public BasicDBObject toJson() {

        return new BasicDBObject()
                .append("id", id)
                .append("description", description)
                .append("url", url)
                .append("os", os)
                .append("countries", countries)
                .append("excluded_os", excludedOs)
                .append("excluded_countries", excludedCountries);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AdvertiseDbObject fromJson(BasicDBObject o) {
        id = o.getInt("id");
        description = o.getString("description");
        url = o.getString("url");
        os = (Set)o.get("os");
        countries = (Set)o.get("countries");
        excludedOs = (Set)o.get("excluded_os");
        excludedCountries = (Set)o.get("excluded_countries");
        return this;
    }
}
