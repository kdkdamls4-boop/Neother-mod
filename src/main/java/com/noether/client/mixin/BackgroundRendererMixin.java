package com.noether.client.mixin;

import com.noether.client.core.ModuleManager;
import com.noether.client.modules.render.CustomFogModule;
import com.noether.client.util.ColorUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Inject(method = "applyFog", at = @At("TAIL"))
    private static void noether$fog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance,
                                    boolean thickFog, float tickDelta, CallbackInfo ci) {
        CustomFogModule module = ModuleManager.getInstance().get(CustomFogModule.class);
        if (module == null || !module.isEnabled()) return;
        if (fogType == BackgroundRenderer.FogType.WATER && !module.water.getBool()) return;
        float d = module.distance.getFloat();
        RenderSystem.setShaderFogStart(viewDistance * d * 0.25f);
        RenderSystem.setShaderFogEnd(viewDistance * d);
        RenderSystem.setShaderFogShape(FogShape.SPHERE);
        int c = module.color.getRgb();
        RenderSystem.setShaderFogColor(
                ColorUtils.red(c) / 255f,
                ColorUtils.green(c) / 255f,
                ColorUtils.blue(c) / 255f
        );
    }

    @Inject(method = "render", at = @At("TAIL"))
    private static void noether$sky(Camera camera, float tickDelta, ClientWorld world, int viewDistance,
                                    float skyDarkness, CallbackInfo ci) {
        CustomFogModule module = ModuleManager.getInstance().get(CustomFogModule.class);
        if (module == null || !module.isEnabled() || !module.sky.getBool()) return;
        int c = module.color.getRgb();
        RenderSystem.clearColor(
                ColorUtils.red(c) / 255f,
                ColorUtils.green(c) / 255f,
                ColorUtils.blue(c) / 255f,
                1.0f
        );
    }
}
