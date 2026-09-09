package com.noether.client.mixin;

import com.noether.client.core.ModuleManager;
import com.noether.client.gui.hud.HudManager;
import com.noether.client.modules.misc.MiddleClickFriendModule;
import com.noether.client.modules.render.FreeCamModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private double cursorDeltaX;

    @Shadow
    private double cursorDeltaY;

    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void noether$middle(long window, int button, int action, int mods, CallbackInfo ci) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE && action == GLFW.GLFW_PRESS) {
            if (client.crosshairTarget instanceof EntityHitResult hit
                    && hit.getEntity() instanceof PlayerEntity player) {
                MiddleClickFriendModule module = ModuleManager.getInstance().get(MiddleClickFriendModule.class);
                if (module != null) module.onMiddleClick(player);
            }
        }
        double scale = client.getWindow().getScaleFactor();
        double mx = client.mouse.getX() / scale;
        double my = client.mouse.getY() / scale;
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
            HudManager.getInstance().mouseClicked(mx, my);
        }
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
            HudManager.getInstance().mouseReleased();
        }
    }

    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void noether$freecamLook(CallbackInfo ci) {
        FreeCamModule cam = ModuleManager.getInstance().get(FreeCamModule.class);
        if (cam != null && cam.isEnabled() && client.mouse.isCursorLocked()) {
            cam.addRotation(this.cursorDeltaX, this.cursorDeltaY);
            this.cursorDeltaX = 0;
            this.cursorDeltaY = 0;
        }
        HudManager.getInstance().mouseDragged(
                client.mouse.getX() / client.getWindow().getScaleFactor(),
                client.mouse.getY() / client.getWindow().getScaleFactor());
    }
}
