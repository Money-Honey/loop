package com.loopme.webapp.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.loopme.webapp.dao.AppDao;
import com.loopme.webapp.dao.impl.CacheableDaoCoordinator;
import com.loopme.webapp.model.dto.Advertise;
import com.loopme.webapp.model.dto.AdvertiseRequestEvent;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import rx.Observable;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class AppServiceImpl implements AppService {

    static Logger Log = Logger.getLogger(AppServiceImpl.class.getName());

    @Inject
    private AppDao dao;

    @Inject
    private CacheableDaoCoordinator cacheableDaoCoordinator;

    @Override
    public List<Advertise> proposeAdvertises(AdvertiseRequestEvent event) {
        if (event.getLimit() <= 0) {
            return Lists.newArrayList();
        }

        return dao.rxLoadMatchedIds(event)
                .toList()
                .doOnNext(list -> Log.debug("Id list: " + list))
                .doOnNext(Collections::shuffle)
                .map(list -> trimToLimit(event.getLimit(), list))
                .flatMap(list -> {
                    try {
                        return Observable.just(Lists.newArrayList(cacheableDaoCoordinator.loadAll(list)));
                    } catch (Throwable ex) {
                        return Observable.error(ex);
                    }
                })
                .toBlocking()
                .single();
    }

    private List<ObjectId> trimToLimit(int limit, List<ObjectId> idList) {
        Preconditions.checkArgument(limit > 0, "Limit couldn't be zero");

        if (idList.size() == 0 || idList.size() == 1) return idList;

        int listSize = idList.size();

        int subListSize = limit;
        if (subListSize > listSize) {
            subListSize = listSize;
        }

        return idList.subList(0, subListSize);
    }
}
