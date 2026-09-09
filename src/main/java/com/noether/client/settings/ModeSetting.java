package com.noether.client.settings;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting<String> {
    private final List<String> modes;

    public ModeSetting(String name, String description, String value, String... modes) {
        super(name, description, value);
        this.modes = Arrays.asList(modes);
    }

    public List<String> getModes() {
        return modes;
    }

    public boolean is(String mode) {
        return get() != null && get().equalsIgnoreCase(mode);
    }

    public void cycle() {
        if (modes.isEmpty()) return;
        int idx = modes.indexOf(get());
        int next = (idx + 1) % modes.size();
        set(modes.get(next));
    }

    @Override
    public void set(String value) {
        if (value == null) return;
        for (String mode : modes) {
            if (mode.equalsIgnoreCase(value)) {
                super.set(mode);
                return;
            }
        }
    }

    @Override
    public Object toConfig() {
        return get();
    }

    @Override
    public void fromConfig(Object raw) {
        if (raw != null) {
            set(String.valueOf(raw));
        }
    }
}
