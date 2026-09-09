package com.noether.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.List;

public final class Render3DUtils {
    private Render3DUtils() {}

    public static void begin() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.lineWidth(1.5f);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
    }

    public static void end() {
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.lineWidth(1.0f);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    private static Matrix4f camRelative(MatrixStack matrices) {
        Vec3d cam = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        matrices.push();
        matrices.translate(-cam.x, -cam.y, -cam.z);
        return matrices.peek().getPositionMatrix();
    }

    public static void box(MatrixStack matrices, Box box, int color, boolean fill) {
        begin();
        Matrix4f m = camRelative(matrices);
        float a = ((color >> 24) & 255) / 255f;
        float r = ((color >> 16) & 255) / 255f;
        float g = ((color >> 8) & 255) / 255f;
        float b = (color & 255) / 255f;
        if (a <= 0) a = fill ? 0.22f : 1f;
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        if (fill) {
            buf.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            quad(buf, m, box, r, g, b, a * 0.35f);
            tess.draw();
        }
        buf.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        lineBox(buf, m, box, r, g, b, Math.max(a, 0.85f));
        tess.draw();
        matrices.pop();
        end();
    }

    public static void line(MatrixStack matrices, Vec3d from, Vec3d to, int color) {
        begin();
        Matrix4f m = camRelative(matrices);
        float a = ((color >> 24) & 255) / 255f;
        if (a <= 0) a = 1f;
        float r = ((color >> 16) & 255) / 255f;
        float g = ((color >> 8) & 255) / 255f;
        float b = (color & 255) / 255f;
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        buf.vertex(m, (float) from.x, (float) from.y, (float) from.z).color(r, g, b, a).next();
        buf.vertex(m, (float) to.x, (float) to.y, (float) to.z).color(r, g, b, a).next();
        tess.draw();
        matrices.pop();
        end();
    }

    public static void circle(MatrixStack matrices, double x, double y, double z, float radius, int color) {
        begin();
        Matrix4f m = camRelative(matrices);
        float a = ((color >> 24) & 255) / 255f;
        if (a <= 0) a = 1f;
        float r = ((color >> 16) & 255) / 255f;
        float g = ((color >> 8) & 255) / 255f;
        float b = (color & 255) / 255f;
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        int segs = 40;
        for (int i = 0; i <= segs; i++) {
            double ang = i * Math.PI * 2 / segs;
            buf.vertex(m, (float) (x + Math.cos(ang) * radius), (float) y, (float) (z + Math.sin(ang) * radius))
                    .color(r, g, b, a).next();
        }
        tess.draw();
        matrices.pop();
        end();
    }

    public static void cone(MatrixStack matrices, double x, double y, double z, double radius, double height, int color) {
        begin();
        Matrix4f m = camRelative(matrices);
        float a = 0.55f;
        float r = ((color >> 16) & 255) / 255f;
        float g = ((color >> 8) & 255) / 255f;
        float b = (color & 255) / 255f;
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        int segs = 24;
        for (int i = 0; i < segs; i++) {
            double a0 = i * Math.PI * 2 / segs;
            double a1 = (i + 1) * Math.PI * 2 / segs;
            float x0 = (float) (x + Math.cos(a0) * radius);
            float z0 = (float) (z + Math.sin(a0) * radius);
            float x1 = (float) (x + Math.cos(a1) * radius);
            float z1 = (float) (z + Math.sin(a1) * radius);
            buf.vertex(m, x0, (float) y, z0).color(r, g, b, a).next();
            buf.vertex(m, x1, (float) y, z1).color(r, g, b, a).next();
            buf.vertex(m, x0, (float) y, z0).color(r, g, b, a).next();
            buf.vertex(m, (float) x, (float) (y + height), (float) z).color(r, g, b, a).next();
        }
        tess.draw();
        matrices.pop();
        end();
    }

    public static void ribbon(MatrixStack matrices, List<Vec3d> points, int color) {
        if (points.size() < 2) return;
        begin();
        Matrix4f m = camRelative(matrices);
        float r = ((color >> 16) & 255) / 255f;
        float g = ((color >> 8) & 255) / 255f;
        float b = (color & 255) / 255f;
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        int n = points.size();
        for (int i = 0; i < n; i++) {
            Vec3d p = points.get(i);
            float t = i / (float) (n - 1);
            float alpha = t * 0.65f;
            float half = 0.08f + t * 0.04f;
            buf.vertex(m, (float) p.x, (float) (p.y + half), (float) p.z).color(r, g, b, alpha).next();
            buf.vertex(m, (float) p.x, (float) (p.y - half * 0.2), (float) p.z).color(r, g, b, alpha * 0.3f).next();
        }
        tess.draw();
        matrices.pop();
        end();
    }

    private static void quad(BufferBuilder buf, Matrix4f m, Box box, float r, float g, float b, float a) {
        float x1 = (float) box.minX, y1 = (float) box.minY, z1 = (float) box.minZ;
        float x2 = (float) box.maxX, y2 = (float) box.maxY, z2 = (float) box.maxZ;
        // bottom
        buf.vertex(m, x1, y1, z1).color(r, g, b, a).next();
        buf.vertex(m, x2, y1, z1).color(r, g, b, a).next();
        buf.vertex(m, x2, y1, z2).color(r, g, b, a).next();
        buf.vertex(m, x1, y1, z2).color(r, g, b, a).next();
        // top
        buf.vertex(m, x1, y2, z1).color(r, g, b, a).next();
        buf.vertex(m, x1, y2, z2).color(r, g, b, a).next();
        buf.vertex(m, x2, y2, z2).color(r, g, b, a).next();
        buf.vertex(m, x2, y2, z1).color(r, g, b, a).next();
        // north
        buf.vertex(m, x1, y1, z1).color(r, g, b, a).next();
        buf.vertex(m, x1, y2, z1).color(r, g, b, a).next();
        buf.vertex(m, x2, y2, z1).color(r, g, b, a).next();
        buf.vertex(m, x2, y1, z1).color(r, g, b, a).next();
        // south
        buf.vertex(m, x1, y1, z2).color(r, g, b, a).next();
        buf.vertex(m, x2, y1, z2).color(r, g, b, a).next();
        buf.vertex(m, x2, y2, z2).color(r, g, b, a).next();
        buf.vertex(m, x1, y2, z2).color(r, g, b, a).next();
        // west
        buf.vertex(m, x1, y1, z1).color(r, g, b, a).next();
        buf.vertex(m, x1, y1, z2).color(r, g, b, a).next();
        buf.vertex(m, x1, y2, z2).color(r, g, b, a).next();
        buf.vertex(m, x1, y2, z1).color(r, g, b, a).next();
        // east
        buf.vertex(m, x2, y1, z1).color(r, g, b, a).next();
        buf.vertex(m, x2, y2, z1).color(r, g, b, a).next();
        buf.vertex(m, x2, y2, z2).color(r, g, b, a).next();
        buf.vertex(m, x2, y1, z2).color(r, g, b, a).next();
    }

    private static void lineBox(BufferBuilder buf, Matrix4f m, Box box, float r, float g, float b, float a) {
        float x1 = (float) box.minX, y1 = (float) box.minY, z1 = (float) box.minZ;
        float x2 = (float) box.maxX, y2 = (float) box.maxY, z2 = (float) box.maxZ;
        edge(buf, m, x1, y1, z1, x2, y1, z1, r, g, b, a);
        edge(buf, m, x2, y1, z1, x2, y1, z2, r, g, b, a);
        edge(buf, m, x2, y1, z2, x1, y1, z2, r, g, b, a);
        edge(buf, m, x1, y1, z2, x1, y1, z1, r, g, b, a);
        edge(buf, m, x1, y2, z1, x2, y2, z1, r, g, b, a);
        edge(buf, m, x2, y2, z1, x2, y2, z2, r, g, b, a);
        edge(buf, m, x2, y2, z2, x1, y2, z2, r, g, b, a);
        edge(buf, m, x1, y2, z2, x1, y2, z1, r, g, b, a);
        edge(buf, m, x1, y1, z1, x1, y2, z1, r, g, b, a);
        edge(buf, m, x2, y1, z1, x2, y2, z1, r, g, b, a);
        edge(buf, m, x2, y1, z2, x2, y2, z2, r, g, b, a);
        edge(buf, m, x1, y1, z2, x1, y2, z2, r, g, b, a);
    }

    private static void edge(BufferBuilder buf, Matrix4f m, float x1, float y1, float z1, float x2, float y2, float z2,
                             float r, float g, float b, float a) {
        buf.vertex(m, x1, y1, z1).color(r, g, b, a).next();
        buf.vertex(m, x2, y2, z2).color(r, g, b, a).next();
    }
}
