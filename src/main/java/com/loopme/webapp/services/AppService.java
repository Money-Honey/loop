package com.loopme.webapp.services;

import com.loopme.webapp.model.dto.Advertise;
import com.loopme.webapp.model.dto.AdvertiseRequestEvent;

import java.util.List;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public interface AppService {
    List<Advertise> proposeAdvertises(AdvertiseRequestEvent event);
}
