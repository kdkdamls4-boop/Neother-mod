package com.noether.client.render;

import net.minecraft.client.render.VertexConsumer;

public class ChamsVertexConsumer implements VertexConsumer {
    private final VertexConsumer delegate;
    private final int color;

    public ChamsVertexConsumer(VertexConsumer delegate, int argb) {
        this.delegate = delegate;
        this.color = argb;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        delegate.vertex(x, y, z);
        return this;
    }

    @Override
    public VertexConsumer color(int r, int g, int b, int a) {
        int cr = (color >> 16) & 255;
        int cg = (color >> 8) & 255;
        int cb = color & 255;
        int ca = (color >> 24) & 255;
        delegate.color(cr, cg, cb, ca == 0 ? 255 : ca);
        return this;
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        delegate.texture(u, v);
        return this;
    }

    @Override
    public VertexConsumer overlay(int u, int v) {
        delegate.overlay(u, v);
        return this;
    }

    @Override
    public VertexConsumer light(int u, int v) {
        delegate.light(u, v);
        return this;
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        delegate.normal(x, y, z);
        return this;
    }

    @Override
    public void next() {
        delegate.next();
    }

    @Override
    public void fixedColor(int r, int g, int b, int a) {
        delegate.fixedColor(r, g, b, a);
    }

    @Override
    public void unfixColor() {
        delegate.unfixColor();
    }
}
