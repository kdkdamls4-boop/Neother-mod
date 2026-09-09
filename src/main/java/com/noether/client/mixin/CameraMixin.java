package com.noether.client.mixin;

import com.noether.client.core.ModuleManager;
import com.noether.client.modules.render.FreeCamModule;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    private boolean ready;

    @Shadow
    private boolean thirdPerson;

    @Shadow
    protected abstract void setPos(double x, double y, double z);

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void noether$freecam(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView,
                                 float tickDelta, CallbackInfo ci) {
        FreeCamModule cam = ModuleManager.getInstance().get(FreeCamModule.class);
        if (cam == null || !cam.isEnabled() || cam.getCamPos() == null) return;
        Vec3d p = cam.getCamPos();
        this.ready = true;
        this.thirdPerson = true;
        setPos(p.x, p.y, p.z);
        setRotation(cam.getYaw(), cam.getPitch());
        ci.cancel();
    }
}
