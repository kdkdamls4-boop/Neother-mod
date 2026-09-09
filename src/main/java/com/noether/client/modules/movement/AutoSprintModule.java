package com.noether.client.modules.movement;

import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import net.minecraft.client.MinecraftClient;

public class AutoSprintModule extends Module {
    public AutoSprintModule() {
        super("AutoSprint", "Постоянный бег без двойного нажатия W", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.player.forwardSpeed > 0 && !client.player.isSneaking() && !client.player.horizontalCollision) {
            client.player.setSprinting(true);
        }
    }
}
