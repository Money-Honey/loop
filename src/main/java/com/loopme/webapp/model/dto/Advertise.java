package com.loopme.webapp.model.dto;

import com.mongodb.BasicDBObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
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
