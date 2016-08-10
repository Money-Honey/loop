package com.loopme.webapp.reader;

/**
 * @author <a href="mailto:vdema@luxoft.com">Vladimir Dema</a>
 */
public class Property {
    private final String property;

    public Property(String property) {
        this.property = property;
    }

    public String asString() {
        return property;
    }

    public int asInt() {
        return Integer.parseInt(property);
    }

    public int asInt(int defaultValue) {
        try {
            return asInt();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean asBoolean() {
        return Boolean.parseBoolean(property);
    }

    public boolean asBoolean(boolean defaultValue) {
        try {
            return asBoolean();
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
