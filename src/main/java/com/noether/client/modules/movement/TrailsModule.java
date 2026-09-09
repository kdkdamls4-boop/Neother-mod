package com.noether.client.modules.movement;

import com.noether.client.config.ThemeManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.render.Render3DUtils;
import com.noether.client.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class TrailsModule extends Module {
    private final NumberSetting length = add(new NumberSetting("Length", "Длина ленты", 24, 8, 64, 1));
    private final List<Vec3d> points = new ArrayList<>();

    public TrailsModule() {
        super("Trails", "Шлейф движения (Ribbon-лента с time-based alpha)", Category.MOVEMENT);
    }

    @Override
    public void onDisable() {
        points.clear();
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        points.add(client.player.getPos().add(0, 0.12, 0));
        int max = length.getInt();
        while (points.size() > max) {
            points.remove(0);
        }
    }

    public void render(MatrixStack matrices) {
        if (!isEnabled() || points.size() < 2) return;
        Render3DUtils.ribbon(matrices, points, ThemeManager.accent());
    }
}
