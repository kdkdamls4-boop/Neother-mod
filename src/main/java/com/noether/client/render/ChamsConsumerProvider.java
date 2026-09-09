package com.noether.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;

public class ChamsConsumerProvider implements VertexConsumerProvider {
    private final VertexConsumerProvider parent;
    private final int color;

    public ChamsConsumerProvider(VertexConsumerProvider parent, int color) {
        this.parent = parent;
        this.color = color;
    }

    @Override
    public VertexConsumer getBuffer(RenderLayer layer) {
        return new ChamsVertexConsumer(parent.getBuffer(RenderLayer.getArmorGlint()), color);
    }
}
