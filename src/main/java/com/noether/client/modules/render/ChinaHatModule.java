package com.noether.client.modules.render;

import com.noether.client.config.ThemeManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.render.Render3DUtils;
import com.noether.client.settings.BooleanSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class ChinaHatModule extends Module {
    private final BooleanSetting self = add(new BooleanSetting("Self", "На себе", true));

    public ChinaHatModule() {
        super("ChinaHat", "Светящийся конус над головой игрока", Category.RENDER);
    }

    public void render(MatrixStack matrices) {
        if (!isEnabled() || !self.getBool()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.getPerspective().isFirstPerson()) return;
        Render3DUtils.cone(matrices,
                client.player.getX(),
                client.player.getY() + client.player.getHeight() + 0.05,
                client.player.getZ(),
                0.55, 0.28, ThemeManager.accent());
    }
}
