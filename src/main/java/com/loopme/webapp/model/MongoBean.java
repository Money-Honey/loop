package com.loopme.webapp.model;

import com.mongodb.BasicDBObject;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public interface MongoBean<T> {
    BasicDBObject toJson();
    T fromJson(BasicDBObject o);
}
