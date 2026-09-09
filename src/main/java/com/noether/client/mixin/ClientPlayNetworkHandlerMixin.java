package com.noether.client.mixin;

import com.noether.client.core.ModuleManager;
import com.noether.client.modules.combat.TotemPopCounterModule;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow
    private ClientWorld world;

    @Inject(method = "onEntityStatus", at = @At("TAIL"))
    private void noether$pops(EntityStatusS2CPacket packet, CallbackInfo ci) {
        if (world == null) return;
        Entity entity = packet.getEntity(world);
        if (!(entity instanceof PlayerEntity player)) return;
        String name = player.getGameProfile().getName();
        TotemPopCounterModule counter = ModuleManager.getInstance().get(TotemPopCounterModule.class);
        if (counter == null) return;
        byte status = packet.getStatus();
        if (status == EntityStatuses.USE_TOTEM_OF_UNDYING) {
            counter.onPop(name);
        } else if (status == EntityStatuses.ADD_DEATH_PARTICLES) {
            counter.onDeath(name);
        }
    }
}
