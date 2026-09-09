package com.noether.client.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Anchor mixin — ChestStealer runs from the module tick while a container is open.
 * Kept so GUI-related hooks have a stable injection point.
 */
@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Inject(method = "close", at = @At("HEAD"))
    private void noether$close(CallbackInfo ci) {
        // no-op hook
    }
}
