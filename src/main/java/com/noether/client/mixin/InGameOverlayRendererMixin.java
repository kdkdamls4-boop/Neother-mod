package com.noether.client.mixin;

import com.noether.client.core.ModuleManager;
import com.noether.client.modules.player.NoRenderModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void noether$fire(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        NoRenderModule module = ModuleManager.getInstance().get(NoRenderModule.class);
        if (module != null && module.cancelFire()) {
            ci.cancel();
        }
    }
}
