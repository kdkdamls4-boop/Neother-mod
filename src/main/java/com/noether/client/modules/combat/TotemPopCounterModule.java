package com.noether.client.modules.combat;

import com.noether.client.gui.hud.Notification;
import com.noether.client.gui.hud.NotificationManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.BooleanSetting;

import java.util.HashMap;
import java.util.Map;

public class TotemPopCounterModule extends Module {
    private final BooleanSetting notify = add(new BooleanSetting("Notify", "Тост при попе", true));
    private final Map<String, Integer> pops = new HashMap<>();

    public TotemPopCounterModule() {
        super("TotemPopCounter", "Счётчик сбитых тотемов", Category.COMBAT);
    }

    @Override
    public void onDisable() {
        pops.clear();
    }

    public void onPop(String name) {
        if (!isEnabled() || name == null) return;
        int count = pops.merge(name, 1, Integer::sum);
        if (notify.getBool()) {
            NotificationManager.getInstance().push("Totem Pop", name + " §d×" + count, Notification.Type.WARNING, 2600);
        }
    }

    public void onDeath(String name) {
        pops.remove(name);
    }

    public int getPops(String name) {
        return pops.getOrDefault(name, 0);
    }

    public Map<String, Integer> getAll() {
        return pops;
    }
}
