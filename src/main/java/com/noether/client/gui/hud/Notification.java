package com.noether.client.gui.hud;

public class Notification {
    public enum Type {
        INFO, SUCCESS, WARNING, ERROR, MODULE
    }

    private final String title;
    private final String message;
    private final Type type;
    private final long startTime;
    private final long durationMs;
    private boolean fading;

    public Notification(String title, String message, Type type, long durationMs, long startTime) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.durationMs = durationMs;
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public boolean isExpired(long now) {
        return now - startTime >= durationMs;
    }

    public float progress(long now) {
        return Math.min(1.0f, (now - startTime) / (float) durationMs);
    }

    /**
     * Slide-in for the first 180ms, fade-out for the last 220ms.
     */
    public float animAlpha(long now) {
        long age = now - startTime;
        if (age < 180) {
            return age / 180.0f;
        }
        long remaining = durationMs - age;
        if (remaining < 220) {
            return Math.max(0f, remaining / 220.0f);
        }
        return 1.0f;
    }

    public float slide(long now) {
        long age = now - startTime;
        if (age < 180) {
            return 1.0f - age / 180.0f;
        }
        return 0f;
    }

    public boolean isFading() {
        return fading;
    }

    public void setFading(boolean fading) {
        this.fading = fading;
    }
}
