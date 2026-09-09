package com.noether.client.modules.render;

import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class FreeCamModule extends Module {
    private final NumberSetting speed = add(new NumberSetting("Speed", "Скорость", 1.2, 0.2, 5.0, 0.1));

    private Vec3d origin;
    private Vec3d camPos;
    private float yaw;
    private float pitch;
    private boolean freezePackets = true;

    public FreeCamModule() {
        super("FreeCam", "Свободный полёт камеры сквозь блоки", Category.RENDER);
        setKeybind(GLFW.GLFW_KEY_F);
    }

    @Override
    protected void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            setEnabled(false);
            return;
        }
        origin = client.player.getPos();
        camPos = client.player.getEyePos();
        yaw = client.player.getYaw();
        pitch = client.player.getPitch();
        freezePackets = true;
    }

    @Override
    protected void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && origin != null) {
            client.player.setPosition(origin.x, origin.y, origin.z);
        }
        freezePackets = false;
        origin = null;
        camPos = null;
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || camPos == null) return;
        float sp = speed.getFloat();
        if (client.options.sprintKey.isPressed()) sp *= 2.5f;
        Vec3d look = Vec3d.fromPolar(pitch, yaw).multiply(sp * 0.35);
        Vec3d right = Vec3d.fromPolar(0, yaw + 90).multiply(sp * 0.35);
        if (client.options.forwardKey.isPressed()) camPos = camPos.add(look);
        if (client.options.backKey.isPressed()) camPos = camPos.subtract(look);
        if (client.options.leftKey.isPressed()) camPos = camPos.subtract(right);
        if (client.options.rightKey.isPressed()) camPos = camPos.add(right);
        if (client.options.jumpKey.isPressed()) camPos = camPos.add(0, sp * 0.35, 0);
        if (client.options.sneakKey.isPressed()) camPos = camPos.add(0, -sp * 0.35, 0);
        client.player.setVelocity(Vec3d.ZERO);
    }

    public boolean shouldFreezePackets() {
        return isEnabled() && freezePackets;
    }

    public Vec3d getCamPos() {
        return camPos;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void addRotation(double dx, double dy) {
        yaw += (float) dx * 0.15f;
        pitch = com.noether.client.core.RotationManager.clampPitch(pitch + (float) dy * 0.15f);
    }
}
