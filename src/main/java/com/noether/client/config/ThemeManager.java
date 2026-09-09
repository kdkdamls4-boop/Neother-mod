package com.noether.client.config;

import com.noether.client.util.ColorUtils;

public final class ThemeManager {
    public enum Theme {
        NEON_VIOLET("Neon Violet", 0xFFA78BFA, 0xFF6D28D9, 0xFF1A1028),
        CYBER_CYAN("Cyber Cyan", 0xFF22D3EE, 0xFF0891B2, 0xFF042F2E),
        SUNSET_ORANGE("Sunset Orange", 0xFFFB923C, 0xFFF43F5E, 0xFF1C1008),
        RAINBOW("Rainbow", 0xFFFFFFFF, 0xFFEC4899, 0xFF111111),
        CUSTOM("Custom RGB", 0xFFA78BFA, 0xFF6D28D9, 0xFF111111);

        private final String display;
        private final int accent;
        private final int accentDark;
        private final int background;

        Theme(String display, int accent, int accentDark, int background) {
            this.display = display;
            this.accent = accent;
            this.accentDark = accentDark;
            this.background = background;
        }

        public String getDisplay() {
            return display;
        }

        public int getAccent() {
            return accent;
        }

        public int getAccentDark() {
            return accentDark;
        }

        public int getBackground() {
            return background;
        }
    }

    private static Theme theme = Theme.NEON_VIOLET;
    private static int customColor = 0xFFA78BFA;

    private ThemeManager() {}

    public static Theme getTheme() {
        return theme;
    }

    public static void setTheme(Theme value) {
        if (value != null) {
            theme = value;
        }
    }

    public static void setTheme(String name) {
        if (name == null) return;
        for (Theme t : Theme.values()) {
            if (t.name().equalsIgnoreCase(name) || t.getDisplay().equalsIgnoreCase(name)) {
                theme = t;
                return;
            }
        }
    }

    public static int getCustomColor() {
        return customColor;
    }

    public static void setCustomColor(int color) {
        customColor = color;
    }

    public static int accent() {
        if (theme == Theme.RAINBOW) {
            return ColorUtils.rainbow(0);
        }
        if (theme == Theme.CUSTOM) {
            return customColor;
        }
        return theme.getAccent();
    }

    public static int accent(float rainbowOffset) {
        if (theme == Theme.RAINBOW) {
            return ColorUtils.rainbow(rainbowOffset);
        }
        if (theme == Theme.CUSTOM) {
            return customColor;
        }
        return theme.getAccent();
    }

    public static int accentDark() {
        return theme.getAccentDark();
    }

    public static int background() {
        return theme.getBackground();
    }

    public static void cycle() {
        Theme[] values = Theme.values();
        theme = values[(theme.ordinal() + 1) % values.length];
    }
}
