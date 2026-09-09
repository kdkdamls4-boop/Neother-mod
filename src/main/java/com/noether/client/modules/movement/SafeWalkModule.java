package com.noether.client.modules.movement;

import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class SafeWalkModule extends Module {
    public SafeWalkModule() {
        super("SafeWalk", "Предотвращение падения с края блоков", Category.MOVEMENT);
    }

    public boolean shouldSneak() {
        if (!isEnabled()) return false;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return false;
        if (!client.player.isOnGround()) return false;
        BlockPos below = BlockPos.ofFloored(client.player.getX(), client.player.getY() - 0.05, client.player.getZ());
        return client.world.getBlockState(below).isAir();
    }
}
