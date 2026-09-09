package com.noether.client.settings;

public class ColorSetting extends Setting<Integer> {
    public ColorSetting(String name, String description, int argb) {
        super(name, description, argb);
    }

    public int getRgb() {
        return get();
    }

    @Override
    public Object toConfig() {
        return get();
    }

    @Override
    public void fromConfig(Object raw) {
        if (raw instanceof Number n) {
            set(n.intValue());
        } else if (raw instanceof String s) {
            String hex = s.startsWith("#") ? s.substring(1) : s;
            try {
                long parsed = Long.parseLong(hex, 16);
                if (hex.length() <= 6) {
                    parsed |= 0xFF000000L;
                }
                set((int) parsed);
            } catch (NumberFormatException ignored) {
            }
        }
    }
}
