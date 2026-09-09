package com.noether.client.mixin;

import com.noether.client.core.ModuleManager;
import com.noether.client.modules.player.AutoRespawnModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {
    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void noether$respawn(CallbackInfo ci) {
        AutoRespawnModule module = ModuleManager.getInstance().get(AutoRespawnModule.class);
        if (module != null && module.isEnabled()) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                client.player.requestRespawn();
                client.setScreen(null);
                ci.cancel();
            }
        }
    }
}
