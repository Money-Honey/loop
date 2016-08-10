package com.loopme.webapp.reader;

import java.util.Properties;

/**
 * @author <a href="mailto:vdema@luxoft.com">Vladimir Dema</a>
 */
public class PropertiesReader {
    private final Properties properties;

    public PropertiesReader(Properties properties) {
        this.properties = properties;
    }

    public Property get(String key) {
        return new Property(properties.getProperty(key));
    }
}
