package com.loopme.webapp.reader;

import java.util.Properties;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
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
