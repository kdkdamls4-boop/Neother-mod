package com.noether.client.settings;

public class NumberSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final double increment;

    public NumberSetting(String name, String description, double value, double min, double max, double increment) {
        super(name, description, value);
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getIncrement() {
        return increment;
    }

    public float getFloat() {
        return get().floatValue();
    }

    public int getInt() {
        return (int) Math.round(get());
    }

    public long getLong() {
        return get().longValue();
    }

    @Override
    public void set(Double value) {
        if (value == null) return;
        double clamped = Math.max(min, Math.min(max, value));
        if (increment > 0) {
            clamped = Math.round(clamped / increment) * increment;
            clamped = Math.max(min, Math.min(max, clamped));
        }
        super.set(clamped);
    }

    @Override
    public Object toConfig() {
        return get();
    }

    @Override
    public void fromConfig(Object raw) {
        if (raw instanceof Number n) {
            set(n.doubleValue());
        } else if (raw instanceof String s) {
            try {
                set(Double.parseDouble(s));
            } catch (NumberFormatException ignored) {
            }
        }
    }
}
