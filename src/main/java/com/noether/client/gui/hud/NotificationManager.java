package com.noether.client.gui.hud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.LongSupplier;

public final class NotificationManager {
    private static final NotificationManager INSTANCE = new NotificationManager();
    private final List<Notification> queue = new ArrayList<>();
    private LongSupplier clock = System::currentTimeMillis;

    private NotificationManager() {}

    public static NotificationManager getInstance() {
        return INSTANCE;
    }

    public void setClock(LongSupplier clock) {
        this.clock = clock == null ? System::currentTimeMillis : clock;
    }

    public void push(String title, String message) {
        push(title, message, Notification.Type.INFO, 2800);
    }

    public void push(String title, String message, Notification.Type type) {
        push(title, message, type, 2800);
    }

    public void push(String title, String message, Notification.Type type, long durationMs) {
        queue.add(new Notification(title, message, type, durationMs, clock.getAsLong()));
    }

    public void moduleToggle(String name, boolean enabled) {
        push(name, enabled ? "Enabled" : "Disabled",
                enabled ? Notification.Type.SUCCESS : Notification.Type.MODULE, 2200);
    }

    public void friend(String name, boolean added) {
        push("Friends", added ? "+ " + name : "- " + name,
                added ? Notification.Type.SUCCESS : Notification.Type.WARNING, 2400);
    }

    public void armorWarning(String piece, int percent) {
        push("Armor", piece + " · " + percent + "%", Notification.Type.ERROR, 3200);
    }

    public void update() {
        update(clock.getAsLong());
    }

    public void update(long now) {
        Iterator<Notification> it = queue.iterator();
        while (it.hasNext()) {
            if (it.next().isExpired(now)) {
                it.remove();
            }
        }
    }

    public List<Notification> getActive() {
        return Collections.unmodifiableList(queue);
    }

    public void clear() {
        queue.clear();
    }
}
