package com.noether.client.mixin;

import com.noether.client.NoetherClient;
import com.noether.client.core.ModuleManager;
import com.noether.client.modules.player.NoRenderModule;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "stop", at = @At("HEAD"))
    private void noether$save(CallbackInfo ci) {
        try {
            if (NoetherClient.getInstance() != null && NoetherClient.getInstance().getConfig() != null) {
                NoetherClient.getInstance().getConfig().save();
            }
        } catch (Exception ignored) {
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void noether$tick(CallbackInfo ci) {
        ModuleManager.getInstance();
        NoRenderModule nr = ModuleManager.getInstance().get(NoRenderModule.class);
        if (nr != null && nr.isEnabled() && nr.nausea.getBool()) {
            MinecraftClient client = (MinecraftClient) (Object) this;
            if (client.player != null) {
                client.player.nauseaIntensity = 0;
                client.player.prevNauseaIntensity = 0;
            }
        }
    }
}
