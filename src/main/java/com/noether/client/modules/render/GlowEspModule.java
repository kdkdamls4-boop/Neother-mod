package com.noether.client.modules.render;

import com.noether.client.config.ThemeManager;
import com.noether.client.friend.FriendManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.BooleanSetting;
import com.noether.client.settings.NumberSetting;
import com.noether.client.util.ColorUtils;
import net.minecraft.entity.player.PlayerEntity;

public class GlowEspModule extends Module {
    public final NumberSetting radius = add(new NumberSetting("Radius", "Толщина контура", 2.0, 1.0, 5.0, 0.5));
    public final BooleanSetting friends = add(new BooleanSetting("Friends Glow", "Подсветка друзей", true));

    public GlowEspModule() {
        super("GlowESP", "Неоновый контур игроков через Stencil", Category.RENDER);
    }

    public int colorFor(PlayerEntity player) {
        if (player != null && FriendManager.getInstance().isFriend(player.getGameProfile().getName())) {
            return ColorUtils.friendGreen();
        }
        return ThemeManager.accent();
    }
}
