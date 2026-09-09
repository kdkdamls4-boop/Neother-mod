package com.noether.client.mixin;

import com.noether.client.core.ModuleManager;
import com.noether.client.modules.player.NoRenderModule;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void noether$hurtCam(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        NoRenderModule module = ModuleManager.getInstance().get(NoRenderModule.class);
        if (module != null && module.cancelHurtCam()) {
            ci.cancel();
        }
    }
}
