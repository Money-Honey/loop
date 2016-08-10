package com.loopme.webapp.model;

import com.mongodb.BasicDBObject;

/**
 * @author <a href="mailto:vdema@luxoft.com">Vladimir Dema</a>
 */
public interface IMongoBean<T> {
    BasicDBObject toJson();
    T fromJson(BasicDBObject o);
}
