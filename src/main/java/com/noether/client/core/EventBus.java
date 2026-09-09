package com.noether.client.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class EventBus {
    private final Map<Class<?>, List<Consumer<?>>> listeners = new ConcurrentHashMap<>();

    public <T> void subscribe(Class<T> type, Consumer<T> listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    @SuppressWarnings("unchecked")
    public <T> void post(T event) {
        if (event == null) return;
        List<Consumer<?>> list = listeners.get(event.getClass());
        if (list == null) return;
        for (Consumer<?> consumer : List.copyOf(list)) {
            ((Consumer<T>) consumer).accept(event);
        }
    }

    public void clear() {
        listeners.clear();
    }
}
