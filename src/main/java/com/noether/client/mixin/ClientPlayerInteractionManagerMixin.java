package com.noether.client.mixin;

import com.noether.client.core.ModuleManager;
import com.noether.client.friend.FriendManager;
import com.noether.client.modules.player.FastPlaceModule;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Shadow
    private int blockBreakingCooldown;

    @Inject(method = "tick", at = @At("HEAD"))
    private void noether$fastPlace(CallbackInfo ci) {
        FastPlaceModule module = ModuleManager.getInstance().get(FastPlaceModule.class);
        if (module != null && module.getDelay() >= 0) {
            this.blockBreakingCooldown = Math.min(this.blockBreakingCooldown, module.getDelay());
        }
    }

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    private void noether$friends(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (target instanceof PlayerEntity victim
                && FriendManager.getInstance().isFriend(victim.getGameProfile().getName())) {
            ci.cancel();
        }
    }
}
