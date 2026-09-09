package com.noether.client.mixin;

import com.noether.client.core.ModuleManager;
import com.noether.client.core.RotationManager;
import com.noether.client.modules.render.FreeCamModule;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    private float noether$yaw;
    private float noether$pitch;
    private boolean noether$rotated;

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void noether$silentHead(CallbackInfo ci) {
        FreeCamModule cam = ModuleManager.getInstance().get(FreeCamModule.class);
        if (cam != null && cam.shouldFreezePackets()) {
            ci.cancel();
            return;
        }
        ClientPlayerEntity self = (ClientPlayerEntity) (Object) this;
        RotationManager rot = RotationManager.getInstance();
        if (rot.isActive()) {
            noether$yaw = self.getYaw();
            noether$pitch = self.getPitch();
            self.setYaw(rot.getYaw());
            self.setPitch(rot.getPitch());
            noether$rotated = true;
            rot.rememberServer(rot.getYaw(), rot.getPitch());
        }
    }

    @Inject(method = "sendMovementPackets", at = @At("RETURN"))
    private void noether$silentTail(CallbackInfo ci) {
        if (!noether$rotated) return;
        ClientPlayerEntity self = (ClientPlayerEntity) (Object) this;
        self.setYaw(noether$yaw);
        self.setPitch(noether$pitch);
        noether$rotated = false;
        RotationManager.getInstance().markSent();
    }
}
