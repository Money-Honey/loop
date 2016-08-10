package com.loopme.webapp.services;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.loopme.webapp.dao.AppDao;
import com.loopme.webapp.dto.Advertise;
import com.loopme.webapp.dto.AdvertiseRequestEvent;

import java.util.List;

/**
 * Created by Volodymyr Dema. Will see.
 */
public class AppServiceImpl implements AppService {

    @Inject
    private AppDao dao;



    @Override
    public List<Advertise> proposeAdvertises(AdvertiseRequestEvent event) {
        if (event.getLimit() == 0) {
            return Lists.newArrayList();
        }



        return dao.retrieveAdvertises(event);
    }
}
