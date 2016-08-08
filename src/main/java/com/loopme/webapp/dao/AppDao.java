package com.loopme.webapp.dao;

import com.loopme.webapp.dto.Advertise;
import com.loopme.webapp.dto.AdvertiseRequestEvent;

import java.util.List;

/**
 * Created by Volodymyr Dema. Will see.
 */
public interface AppDao {
    List<Advertise> retrieveAdvertises(AdvertiseRequestEvent event);
}
