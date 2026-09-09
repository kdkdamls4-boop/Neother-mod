package com.noether.client.mixin;

import com.noether.client.core.ModuleManager;
import com.noether.client.modules.movement.SafeWalkModule;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void noether$safewalk(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        SafeWalkModule module = ModuleManager.getInstance().get(SafeWalkModule.class);
        if (module != null && module.shouldSneak()) {
            ((KeyboardInput) (Object) this).sneaking = true;
        }
    }
}
