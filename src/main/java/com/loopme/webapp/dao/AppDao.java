package com.loopme.webapp.dao;

import com.loopme.webapp.model.dto.Advertise;
import com.loopme.webapp.model.dto.AdvertiseRequestEvent;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * Created by Volodymyr Dema. Will see.
 */
public interface AppDao {
    Map<ObjectId, Advertise> loadRecordsByIdList(List<ObjectId> idList);
    List<ObjectId> loadMatchedIds(AdvertiseRequestEvent event);
}
