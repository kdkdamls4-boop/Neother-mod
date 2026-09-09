package com.noether.client.mixin;

import com.noether.client.core.ModuleManager;
import com.noether.client.modules.render.ChamsModule;
import com.noether.client.modules.render.GlowEspModule;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"))
    private void noether$chamsHead(T entity, float yaw, float tickDelta, MatrixStack matrices,
                                   VertexConsumerProvider consumers, int light, CallbackInfo ci) {
        ChamsModule chams = ModuleManager.getInstance().get(ChamsModule.class);
        if (chams != null && chams.isEnabled() && entity instanceof PlayerEntity player) {
            if (!chams.self.getBool() && player.isMainPlayer()) return;
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
            GL11.glDepthFunc(GL11.GL_GREATER);
            if (chams.mode.is("Wireframe")) {
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            }
        }
        GlowEspModule glow = ModuleManager.getInstance().get(GlowEspModule.class);
        if (glow != null && glow.isEnabled() && entity instanceof PlayerEntity) {
            entity.setGlowing(true);
        }
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("RETURN"))
    private void noether$chamsTail(T entity, float yaw, float tickDelta, MatrixStack matrices,
                                   VertexConsumerProvider consumers, int light, CallbackInfo ci) {
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glPolygonOffset(1.0f, 1100000.0f);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GlowEspModule glow = ModuleManager.getInstance().get(GlowEspModule.class);
        if ((glow == null || !glow.isEnabled()) && entity instanceof PlayerEntity player && !player.isGlowing()) {
            entity.setGlowing(false);
        }
    }
}
