package com.noether.client.settings;

public abstract class Setting<T> {
    private final String name;
    private final String description;
    private T value;
    private final T defaultValue;

    protected Setting(String name, String description, T value) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.defaultValue = value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T get() {
        return value;
    }

    public T getValue() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getDefault() {
        return defaultValue;
    }

    public void reset() {
        this.value = defaultValue;
    }

    public abstract Object toConfig();

    public abstract void fromConfig(Object raw);
}
