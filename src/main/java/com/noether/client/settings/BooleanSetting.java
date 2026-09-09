package com.noether.client.settings;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, String description, boolean value) {
        super(name, description, value);
    }

    public boolean getBool() {
        return Boolean.TRUE.equals(get());
    }

    public void toggle() {
        set(!getBool());
    }

    @Override
    public Object toConfig() {
        return getBool();
    }

    @Override
    public void fromConfig(Object raw) {
        if (raw instanceof Boolean b) {
            set(b);
        } else if (raw instanceof String s) {
            set(Boolean.parseBoolean(s));
        } else if (raw instanceof Number n) {
            set(n.intValue() != 0);
        }
    }
}
