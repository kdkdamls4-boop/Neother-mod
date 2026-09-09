package com.noether.client.mixin;

import com.noether.client.core.ModuleManager;
import com.noether.client.modules.render.FullBrightModule;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @ModifyVariable(method = "update", at = @At("STORE"), ordinal = 6)
    private float noether$gamma(float gamma) {
        FullBrightModule module = ModuleManager.getInstance().get(FullBrightModule.class);
        if (module != null && module.isLocked()) {
            return 16.0f;
        }
        return gamma;
    }
}
