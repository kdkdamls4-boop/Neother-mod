package com.noether.client.modules.render;

import com.noether.client.friend.FriendManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.BooleanSetting;
import com.noether.client.settings.ColorSetting;
import com.noether.client.settings.ModeSetting;
import com.noether.client.util.ColorUtils;
import net.minecraft.entity.player.PlayerEntity;

public class ChamsModule extends Module {
    public final ModeSetting mode = add(new ModeSetting("Mode", "Стиль", "Flat", "Flat", "Wireframe", "Lighting", "Texture"));
    public final ModeSetting colorMode = add(new ModeSetting("Color Mode", "Цвет", "Custom", "Custom", "Rainbow", "Wave"));
    public final ColorSetting visible = add(new ColorSetting("Visible", "Видимый цвет", 0xFFA78BFA));
    public final ColorSetting hidden = add(new ColorSetting("Hidden", "Цвет сквозь стены", 0x66EC4899));
    public final ColorSetting waveFrom = add(new ColorSetting("Wave A", "Градиент A", 0xFFA78BFA));
    public final ColorSetting waveTo = add(new ColorSetting("Wave B", "Градиент B", 0xFF22D3EE));
    public final BooleanSetting players = add(new BooleanSetting("Players", "Игроки", true));
    public final BooleanSetting self = add(new BooleanSetting("Self", "Себя", false));

    public ChamsModule() {
        super("Chams", "Шейдерный рендер сущностей сквозь стены", Category.RENDER);
    }

    public int colorFor(PlayerEntity player, boolean throughWalls, float offset) {
        boolean friend = player != null && FriendManager.getInstance().isFriend(player.getGameProfile().getName());
        int modeIdx = colorMode.is("Rainbow") ? 1 : colorMode.is("Wave") ? 2 : 0;
        int base = ColorUtils.chamsColor(visible.getRgb(), waveFrom.getRgb(), waveTo.getRgb(),
                modeIdx, friend, offset, System.currentTimeMillis());
        if (throughWalls) {
            return ColorUtils.withAlpha(base, ColorUtils.alpha(hidden.getRgb()));
        }
        return base;
    }
}
