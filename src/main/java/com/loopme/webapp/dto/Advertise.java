package com.loopme.webapp.dto;

import com.mongodb.BasicDBObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Volodymyr Dema. Will see.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Advertise {
    int id;
    String description;
    String url;

    public Advertise fromJson(BasicDBObject obj) {
        id = obj.getInt("id");
        description = obj.getString("description");
        url = obj.getString("url");

        return this;
    }
}
