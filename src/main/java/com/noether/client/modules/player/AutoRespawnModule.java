package com.noether.client.modules.player;

import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import net.minecraft.client.MinecraftClient;

public class AutoRespawnModule extends Module {
    public AutoRespawnModule() {
        super("AutoRespawn", "Мгновенное возрождение без экрана смерти", Category.PLAYER);
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.player.isDead()) {
            client.player.requestRespawn();
            client.setScreen(null);
        }
    }
}
