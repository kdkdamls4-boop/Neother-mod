package com.noether.client.modules.player;

import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.BooleanSetting;

public class NoRenderModule extends Module {
    public final BooleanSetting hurtCam = add(new BooleanSetting("HurtCam", "Убрать тряску камеры", true));
    public final BooleanSetting fire = add(new BooleanSetting("Fire", "Убрать огонь на экране", true));
    public final BooleanSetting blindness = add(new BooleanSetting("Blindness", "Игнор слепоты", true));
    public final BooleanSetting darkness = add(new BooleanSetting("Darkness", "Игнор тьмы", true));
    public final BooleanSetting explosions = add(new BooleanSetting("Explosions", "Скрыть взрывы", false));
    public final BooleanSetting pumpkin = add(new BooleanSetting("Pumpkin", "Скрыть тыкву", true));
    public final BooleanSetting nausea = add(new BooleanSetting("Nausea", "Убрать тошноту", true));

    public NoRenderModule() {
        super("NoRender", "Отключение HurtCam, Fire, Blindness, Darkness, Explosions, Pumpkin", Category.PLAYER);
    }

    public boolean cancelHurtCam() {
        return isEnabled() && hurtCam.getBool();
    }

    public boolean cancelFire() {
        return isEnabled() && fire.getBool();
    }

    public boolean cancelPumpkin() {
        return isEnabled() && pumpkin.getBool();
    }
}
