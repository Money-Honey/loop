package com.loopme.webapp.dao;

import com.loopme.webapp.model.dto.Advertise;
import com.loopme.webapp.model.dto.AdvertiseRequestEvent;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:vdema@luxoft.com">Vladimir Dema</a>
 */
public interface IRequestLayerDao {
    Map<ObjectId, Advertise> loadRecordsByIdList(List<ObjectId> idList);
    List<ObjectId> loadMatchedIds(AdvertiseRequestEvent event);
}
