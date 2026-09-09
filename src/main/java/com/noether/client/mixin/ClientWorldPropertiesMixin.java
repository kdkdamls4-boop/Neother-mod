package com.noether.client.mixin;

import com.noether.client.core.ModuleManager;
import com.noether.client.modules.render.CustomTimeModule;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.Properties.class)
public class ClientWorldPropertiesMixin {
    @Inject(method = "getTimeOfDay", at = @At("HEAD"), cancellable = true)
    private void noether$time(CallbackInfoReturnable<Long> cir) {
        CustomTimeModule module = ModuleManager.getInstance().get(CustomTimeModule.class);
        if (module != null && module.isEnabled()) {
            cir.setReturnValue(module.getTime());
        }
    }
}
