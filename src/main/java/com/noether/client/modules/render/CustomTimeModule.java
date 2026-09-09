package com.noether.client.modules.render;

import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.ModeSetting;
import com.noether.client.settings.NumberSetting;

public class CustomTimeModule extends Module {
    private final ModeSetting mode = add(new ModeSetting("Mode", "Пресет", "Night", "Day", "Noon", "Sunset", "Night", "Custom"));
    private final NumberSetting custom = add(new NumberSetting("Custom", "Кастомное время (0-24000)", 18000, 0, 24000, 100));

    public CustomTimeModule() {
        super("CustomTime", "Фиксация времени суток на клиенте", Category.RENDER);
    }

    public long getTime() {
        return switch (mode.get()) {
            case "Day" -> 1000L;
            case "Noon" -> 6000L;
            case "Sunset" -> 12000L;
            case "Custom" -> custom.getLong();
            default -> 18000L;
        };
    }
}
