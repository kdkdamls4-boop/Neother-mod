package com.noether.client.modules.render;

import com.noether.client.config.ThemeManager;
import com.noether.client.friend.FriendManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.BooleanSetting;
import com.noether.client.settings.NumberSetting;
import com.noether.client.util.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GlowEspModule extends Module {
    public final NumberSetting radius = add(new NumberSetting("Radius", "Толщина контура", 2.0, 1.0, 5.0, 0.5));
    public final BooleanSetting friends = add(new BooleanSetting("Friends Glow", "Подсветка друзей", true));

    private final Map<UUID, Boolean> originalGlow = new HashMap<>();

    public GlowEspModule() {
        super("GlowESP", "Неоновый контур игроков через Stencil", Category.RENDER);
    }

    public void tag(LivingEntity entity) {
        if (entity == null) return;
        originalGlow.putIfAbsent(entity.getUuid(), entity.isGlowing());
        entity.setGlowing(true);
    }

    public void restore(LivingEntity entity) {
        if (entity == null) return;
        Boolean was = originalGlow.remove(entity.getUuid());
        if (was != null) {
            entity.setGlowing(was);
        }
    }

    @Override
    protected void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null) {
            for (PlayerEntity player : client.world.getPlayers()) {
                Boolean was = originalGlow.remove(player.getUuid());
                if (was != null) {
                    player.setGlowing(was);
                }
            }
        }
        originalGlow.clear();
    }

    public int colorFor(PlayerEntity player) {
        if (player != null && FriendManager.getInstance().isFriend(player.getGameProfile().getName())) {
            return ColorUtils.friendGreen();
        }
        return ThemeManager.accent();
    }
}
