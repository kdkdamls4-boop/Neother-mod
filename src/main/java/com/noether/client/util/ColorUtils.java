package com.noether.client.util;

import java.awt.Color;

/**
 * Color math used by Chams, ESP, HUD and ThemeManager.
 * Pure Java — covered by VisualsCoreTest.
 */
public final class ColorUtils {
    private ColorUtils() {}

    public static int rgba(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public static int rgb(int r, int g, int b) {
        return rgba(r, g, b, 255);
    }

    public static int withAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | ((alpha & 0xFF) << 24);
    }

    public static int red(int color) {
        return (color >> 16) & 0xFF;
    }

    public static int green(int color) {
        return (color >> 8) & 0xFF;
    }

    public static int blue(int color) {
        return color & 0xFF;
    }

    public static int alpha(int color) {
        return (color >> 24) & 0xFF;
    }

    public static int interpolate(int c1, int c2, float t) {
        t = clamp01(t);
        int a = lerp(alpha(c1), alpha(c2), t);
        int r = lerp(red(c1), red(c2), t);
        int g = lerp(green(c1), green(c2), t);
        int b = lerp(blue(c1), blue(c2), t);
        return rgba(r, g, b, a);
    }

    public static int wave(int c1, int c2, float speed) {
        return wave(c1, c2, speed, System.currentTimeMillis());
    }

    public static int wave(int c1, int c2, float speed, long timeMs) {
        float t = (float) (Math.sin(timeMs / 1000.0 * speed) * 0.5 + 0.5);
        return interpolate(c1, c2, t);
    }

    public static int rainbow(float offset) {
        return rainbow(offset, System.currentTimeMillis());
    }

    public static int rainbow(float offset, long timeMs) {
        float hue = ((timeMs % 4000L) / 4000.0f + offset) % 1.0f;
        if (hue < 0) hue += 1.0f;
        return hsb(hue, 0.75f, 1.0f, 255);
    }

    public static int hsb(float hue, float sat, float bri, int alpha) {
        int rgb = Color.HSBtoRGB(hue, sat, bri);
        return withAlpha(rgb, alpha);
    }

    public static int healthColor(float ratio) {
        ratio = clamp01(ratio);
        if (ratio > 0.5f) {
            return interpolate(0xFFFFFF00, 0xFF22C55E, (ratio - 0.5f) / 0.5f);
        }
        return interpolate(0xFFEF4444, 0xFFFFFF00, ratio / 0.5f);
    }

    public static int friendGreen() {
        return 0xFF22C55E;
    }

    /**
     * Chams color selection used by both the module and unit tests.
     * mode: 0 Custom, 1 Rainbow, 2 Wave
     */
    public static int chamsColor(int custom, int waveFrom, int waveTo, int mode, boolean friend, float offset, long timeMs) {
        if (friend) {
            return friendGreen();
        }
        return switch (mode) {
            case 1 -> rainbow(offset, timeMs);
            case 2 -> wave(waveFrom, waveTo, 2.2f, timeMs);
            default -> custom;
        };
    }

    public static float clamp01(float v) {
        return v < 0 ? 0 : (v > 1 ? 1 : v);
    }

    private static int lerp(int a, int b, float t) {
        return Math.round(a + (b - a) * t);
    }
}
