package com.noether.client.mixin;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * World ESP is drawn via Fabric WorldRenderEvents.LAST.
 * Mixin retained as a stable 3D render hook.
 */
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void noether$tick(CallbackInfo ci) {
        // no-op
    }
}
