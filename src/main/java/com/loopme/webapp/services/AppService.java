package com.loopme.webapp.services;

import com.loopme.webapp.dto.Advertise;
import com.loopme.webapp.dto.AdvertiseRequestEvent;

import java.util.List;

/**
 * Created by Volodymyr Dema. Will see.
 */
public interface AppService {
    List<Advertise> proposeAdvertises(AdvertiseRequestEvent event);
}
