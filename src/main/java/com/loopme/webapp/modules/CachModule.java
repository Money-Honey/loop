package com.loopme.webapp.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.loopme.webapp.dao.impl.mongo.Cache;

/**
 * @author <a href="mailto:vdema@luxoft.com">Vladimir Dema</a>
 */
public class CachModule implements Module {
    private final boolean isCachEnabled;

    public CachModule(boolean isCachEnabled) {
        this.isCachEnabled = isCachEnabled;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(Boolean.class).annotatedWith(Names.named("isCacheEnabled")).toInstance(isCachEnabled);
        binder.bind(Cache.class).in(Scopes.SINGLETON);
    }
}
