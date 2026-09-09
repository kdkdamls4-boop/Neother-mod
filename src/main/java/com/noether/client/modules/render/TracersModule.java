package com.noether.client.modules.render;

import com.noether.client.config.ThemeManager;
import com.noether.client.friend.FriendManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.render.Render3DUtils;
import com.noether.client.util.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class TracersModule extends Module {
    public TracersModule() {
        super("Tracers", "Цветные линии к ближайшим врагам", Category.RENDER);
    }

    public void render(MatrixStack matrices) {
        if (!isEnabled()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;
        Vec3d from = client.player.getEyePos();
        for (PlayerEntity player : client.world.getPlayers()) {
            if (player == client.player) continue;
            int color = FriendManager.getInstance().isFriend(player.getGameProfile().getName())
                    ? ColorUtils.friendGreen() : ThemeManager.accent();
            Render3DUtils.line(matrices, from, player.getBoundingBox().getCenter(), color);
        }
    }
}
