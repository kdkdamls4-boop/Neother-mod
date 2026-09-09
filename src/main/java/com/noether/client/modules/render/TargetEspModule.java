package com.noether.client.modules.render;

import com.noether.client.config.ThemeManager;
import com.noether.client.core.TargetManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.render.Render3DUtils;
import com.noether.client.render.RenderUtils;
import com.noether.client.settings.BooleanSetting;
import com.noether.client.util.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class TargetEspModule extends Module {
    private static final double MAX_FALLBACK_RANGE = 64.0;

    private final BooleanSetting rings = add(new BooleanSetting("Rings", "3D кольца", true));
    private final BooleanSetting arrows = add(new BooleanSetting("Offscreen", "Стрелки вне FOV", true));

    public TargetEspModule() {
        super("TargetESP", "3D кольца вокруг цели + Offscreen Arrows", Category.RENDER);
    }

    public void render(MatrixStack matrices) {
        if (!isEnabled() || !rings.getBool()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        LivingEntity target = resolveTarget(client);
        if (target == null) return;

        double yOff = Math.sin(System.currentTimeMillis() / 400.0) * 0.35 + target.getHeight() * 0.5;
        int c1 = ThemeManager.accent(0);
        int c2 = ThemeManager.accent(0.3f);
        Render3DUtils.circle(matrices, target.getX(), target.getY() + yOff, target.getZ(), 0.7, c1);
        Render3DUtils.circle(matrices, target.getX(), target.getY() + yOff + 0.25, target.getZ(), 0.55,
                ColorUtils.interpolate(c1, c2, 0.5f));
    }

    private LivingEntity resolveTarget(MinecraftClient client) {
        if (client.player == null || client.world == null) return null;
        LivingEntity current = TargetManager.getCurrent();
        if (current != null && current.isAlive() && !current.isRemoved()) {
            return current;
        }
        LivingEntity nearest = null;
        double bestSq = MAX_FALLBACK_RANGE * MAX_FALLBACK_RANGE;
        for (PlayerEntity player : client.world.getPlayers()) {
            if (player == client.player || !player.isAlive()) continue;
            double sq = player.squaredDistanceTo(client.player);
            if (sq < bestSq) {
                bestSq = sq;
                nearest = player;
            }
        }
        return nearest;
    }

    public void render2D(DrawContext context) {
        if (!isEnabled() || !arrows.getBool()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;
        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        int cx = sw / 2;
        int cy = sh / 2;
        for (PlayerEntity player : client.world.getPlayers()) {
            if (player == client.player) continue;
            double[] pos = RenderUtils.worldToScreen(player.getX(), player.getY() + 1.0, player.getZ());
            boolean off = pos == null || pos[0] < 0 || pos[1] < 0 || pos[0] > sw || pos[1] > sh;
            if (!off) continue;
            double dx = player.getX() - client.player.getX();
            double dz = player.getZ() - client.player.getZ();
            double yaw = Math.toRadians(client.player.getYaw());
            double rx = dx * Math.cos(yaw) + dz * Math.sin(yaw);
            double rz = -dx * Math.sin(yaw) + dz * Math.cos(yaw);
            double ang = Math.atan2(rx, rz);
            int ax = cx + (int) (Math.sin(ang) * 80);
            int ay = cy - (int) (Math.cos(ang) * 54);
            ax = MathHelper.clamp(ax, 12, sw - 12);
            ay = MathHelper.clamp(ay, 12, sh - 12);
            int dist = (int) client.player.distanceTo(player);
            context.fill(ax - 5, ay - 5, ax + 5, ay + 5, ThemeManager.accent());
            context.drawText(client.textRenderer, dist + "m", ax + 7, ay - 3, 0xFFE5E7EB, true);
        }
    }
}
