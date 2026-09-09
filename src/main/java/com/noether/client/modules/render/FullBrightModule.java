package com.noether.client.modules.render;

import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import net.minecraft.client.MinecraftClient;

public class FullBrightModule extends Module {
    private double previous = 1.0;

    public FullBrightModule() {
        super("FullBright", "Максимальная гамма видимости с gamma-lock", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            previous = client.options.getGamma().getValue();
        }
    }

    @Override
    protected void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            client.options.getGamma().setValue(previous);
        }
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            client.options.getGamma().setValue(16.0);
        }
    }

    public boolean isLocked() {
        return isEnabled();
    }
}
