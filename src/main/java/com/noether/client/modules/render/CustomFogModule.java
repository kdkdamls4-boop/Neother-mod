package com.noether.client.modules.render;

import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.BooleanSetting;
import com.noether.client.settings.ColorSetting;
import com.noether.client.settings.NumberSetting;

public class CustomFogModule extends Module {
    public final ColorSetting color = add(new ColorSetting("Color", "Цвет тумана", 0xFF2E1065));
    public final NumberSetting distance = add(new NumberSetting("Distance", "Дистанция", 0.55, 0.05, 1.5, 0.05));
    public final BooleanSetting water = add(new BooleanSetting("Water", "Подводный туман", true));
    public final BooleanSetting sky = add(new BooleanSetting("Skybox", "Красить небо", true));

    public CustomFogModule() {
        super("CustomFog", "Кастомный цвет и дистанция тумана", Category.RENDER);
    }
}
