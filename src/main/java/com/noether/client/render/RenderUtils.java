package com.noether.client.render;

import com.noether.client.config.ThemeManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public final class RenderUtils {
    private RenderUtils() {}

    public static void roundRect(DrawContext context, int x, int y, int w, int h, int r, int color) {
        r = Math.min(r, Math.min(w, h) / 2);
        context.fill(x + r, y, x + w - r, y + h, color);
        context.fill(x, y + r, x + r, y + h - r, color);
        context.fill(x + w - r, y + r, x + w, y + h - r, color);
        context.fill(x + r, y, x + r + 1, y + 1, color);
        context.fill(x + w - r - 1, y, x + w - r, y + 1, color);
        context.fill(x + r, y + h - 1, x + r + 1, y + h, color);
        context.fill(x + w - r - 1, y + h - 1, x + w - r, y + h, color);
        // corner blobs
        context.fill(x, y + r / 2, x + r, y + r, color);
        context.fill(x, y + h - r, x + r, y + h - r / 2, color);
        context.fill(x + w - r, y + r / 2, x + w, y + r, color);
        context.fill(x + w - r, y + h - r, x + w, y + h - r / 2, color);
    }

    public static void outline(DrawContext context, int x, int y, int w, int h, int color) {
        context.fill(x, y, x + w, y + 1, color);
        context.fill(x, y + h - 1, x + w, y + h, color);
        context.fill(x, y, x + 1, y + h, color);
        context.fill(x + w - 1, y, x + w, y + h, color);
    }

    public static void accentBar(DrawContext context, int x, int y, int w, int h) {
        context.fill(x, y, x + w, y + h, ThemeManager.accent());
    }

    public static void scissor(int x, int y, int w, int h) {
        MinecraftClient client = MinecraftClient.getInstance();
        double scale = client.getWindow().getScaleFactor();
        int fbH = client.getWindow().getFramebufferHeight();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (x * scale), (int) (fbH - (y + h) * scale), (int) (w * scale), (int) (h * scale));
    }

    public static void disableScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    /**
     * Project world coords to scaled screen. Returns null if behind camera.
     */
    public static double[] worldToScreen(double x, double y, double z) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        Vec3d cam = camera.getPos();
        Matrix4f proj = client.gameRenderer.getBasicProjectionMatrix(
                client.options.getFov().getValue().intValue());
        org.joml.Matrix4f view = new Matrix4f()
                .rotation(camera.getRotation().conjugate(new org.joml.Quaternionf()));
        Vector4f pos = new Vector4f((float) (x - cam.x), (float) (y - cam.y), (float) (z - cam.z), 1.0f);
        view.transform(pos);
        proj.transform(pos);
        if (pos.w <= 0.1f) return null;
        pos.div(pos.w);
        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        double sx = (pos.x * 0.5 + 0.5) * sw;
        double sy = (1.0 - (pos.y * 0.5 + 0.5)) * sh;
        return new double[] { sx, sy };
    }
}
