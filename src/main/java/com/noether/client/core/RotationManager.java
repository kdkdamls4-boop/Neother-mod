package com.noether.client.core;

import java.util.UUID;

/**
 * Silent rotations: desired yaw/pitch are applied to movement packets only.
 * {@link #hasBeenSent()} is true after the look packet actually went out —
 * KillAura must wait for that before attacking (GrimAC raytrace).
 */
public final class RotationManager {
    private static final RotationManager INSTANCE = new RotationManager();

    private boolean active;
    private boolean sent;
    private float yaw;
    private float pitch;
    private float serverYaw;
    private float serverPitch;
    private UUID targetId;

    private RotationManager() {}

    public static RotationManager getInstance() {
        return INSTANCE;
    }

    public void set(float yaw, float pitch) {
        set(yaw, pitch, targetId);
    }

    public void set(float yaw, float pitch, UUID target) {
        if (target == null || targetId == null || !target.equals(targetId)) {
            sent = false;
        }
        this.targetId = target;
        this.yaw = yaw;
        this.pitch = pitch;
        this.active = true;
    }

    public void clear() {
        this.active = false;
        this.sent = false;
        this.targetId = null;
    }

    public boolean isActive() {
        return active;
    }

    public void markSent() {
        if (active) {
            this.sent = true;
        }
    }

    public boolean hasBeenSent() {
        return active && sent;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getServerYaw() {
        return serverYaw;
    }

    public float getServerPitch() {
        return serverPitch;
    }

    public void rememberServer(float yaw, float pitch) {
        this.serverYaw = yaw;
        this.serverPitch = pitch;
    }

    public static float[] lookAt(double fromX, double fromY, double fromZ, double toX, double toY, double toZ) {
        double dx = toX - fromX;
        double dy = toY - fromY;
        double dz = toZ - fromZ;
        double dist = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, dist));
        return new float[] { wrap(yaw), clampPitch(pitch) };
    }

    public static float wrap(float yaw) {
        yaw %= 360.0f;
        if (yaw >= 180.0f) yaw -= 360.0f;
        if (yaw < -180.0f) yaw += 360.0f;
        return yaw;
    }

    public static float clampPitch(float pitch) {
        if (pitch > 90) return 90;
        if (pitch < -90) return -90;
        return pitch;
    }
}
