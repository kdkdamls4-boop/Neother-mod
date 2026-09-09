package com.noether.client.modules.player;

import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.NumberSetting;

public class FastPlaceModule extends Module {
    private final NumberSetting delay = add(new NumberSetting("Delay", "Задержка в тиках", 0, 0, 4, 1));

    public FastPlaceModule() {
        super("FastPlace", "Мгновенная установка блоков без задержки", Category.PLAYER);
    }

    public int getDelay() {
        return isEnabled() ? delay.getInt() : -1;
    }
}
