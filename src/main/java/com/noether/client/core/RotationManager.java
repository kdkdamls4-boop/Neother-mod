package com.noether.client.core;

/**
 * Silent rotations: desired yaw/pitch are applied to movement packets only.
 */
public final class RotationManager {
    private static final RotationManager INSTANCE = new RotationManager();

    private boolean active;
    private float yaw;
    private float pitch;
    private float serverYaw;
    private float serverPitch;

    private RotationManager() {}

    public static RotationManager getInstance() {
        return INSTANCE;
    }

    public void set(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.active = true;
    }

    public void clear() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
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
