package com.loopme.webapp.model;

import com.mongodb.BasicDBObject;
import lombok.Data;

import java.util.List;

/**
 * @author <a href="mailto:vdema@luxoft.com">Vladimir Dema</a>
 */
@Data
public class AdvertiseDbObject implements IMongoBean<AdvertiseDbObject> {
    int id;
    String description;
    String url;
    //ToDo: Use Sets
    List<String> os;
    List<String> countries;
    List<String> excludedOs;
    List<String> excludedCountries;

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
        os = (List)o.get("os");
        countries = (List)o.get("countries");
        excludedOs = (List)o.get("excluded_os");
        excludedCountries = (List)o.get("excluded_countries");
        return this;
    }
}
