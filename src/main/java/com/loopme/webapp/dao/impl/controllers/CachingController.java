package com.loopme.webapp.dao.impl.controllers;

/**
 * @author <a href="mailto:vdema@luxoft.com">Vladimir Dema</a>
 */
public class CachingController implements CachingControllerMBean {
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
