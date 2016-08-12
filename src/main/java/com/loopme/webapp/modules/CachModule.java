package com.loopme.webapp.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.loopme.webapp.dao.impl.CacheableDaoCoordinator;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class CachModule implements Module {
    private final boolean isCachEnabled;
    private final int cacheSize, cacheExpireTimeInSeconds;

    public CachModule(boolean isCachEnabled, int cacheSize, int cacheExpireTimeInSeconds) {
        this.isCachEnabled = isCachEnabled;
        this.cacheSize = cacheSize;
        this.cacheExpireTimeInSeconds = cacheExpireTimeInSeconds;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(Boolean.class).annotatedWith(Names.named("isCacheEnabled")).toInstance(isCachEnabled);
        binder.bind(Integer.class).annotatedWith(Names.named("cacheSize")).toInstance(cacheSize);
        binder.bind(Integer.class).annotatedWith(Names.named("expireTimeSecond")).toInstance(cacheExpireTimeInSeconds);

        binder.bind(CacheableDaoCoordinator.class).in(Scopes.SINGLETON);
    }
}
