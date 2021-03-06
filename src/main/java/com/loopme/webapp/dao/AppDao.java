package com.loopme.webapp.dao;

import com.loopme.webapp.model.dto.Advertise;
import com.loopme.webapp.model.dto.AdvertiseRequestEvent;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public interface AppDao {
    Map<ObjectId, Advertise> loadRecordsByIdList(List<ObjectId> idList);
    List<ObjectId> loadMatchedIds(AdvertiseRequestEvent event);
}
