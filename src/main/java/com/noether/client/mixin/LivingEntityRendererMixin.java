package com.noether.client.mixin;

import com.noether.client.core.ModuleManager;
import com.noether.client.friend.FriendManager;
import com.noether.client.modules.render.ChamsModule;
import com.noether.client.modules.render.GlowEspModule;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
    @Unique
    private boolean noether$chamsPushed;

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"))
    private void noether$chamsHead(T entity, float yaw, float tickDelta, MatrixStack matrices,
                                   VertexConsumerProvider consumers, int light, CallbackInfo ci) {
        noether$chamsPushed = false;
        ChamsModule chams = ModuleManager.getInstance().get(ChamsModule.class);
        if (chams != null && chams.isEnabled() && entity instanceof PlayerEntity player
                && chams.players.getBool()
                && (chams.self.getBool() || !player.isMainPlayer())) {
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
            GL11.glDepthFunc(GL11.GL_GREATER);
            if (chams.mode.is("Wireframe")) {
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            }
            noether$chamsPushed = true;
        }

        GlowEspModule glow = ModuleManager.getInstance().get(GlowEspModule.class);
        if (glow != null && entity instanceof PlayerEntity player && !player.isMainPlayer()) {
            boolean friend = FriendManager.getInstance().isFriend(player.getGameProfile().getName());
            if (glow.isEnabled() && (glow.friends.getBool() || !friend)) {
                glow.tag(player);
            } else {
                glow.restore(player);
            }
        }
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("RETURN"))
    private void noether$chamsTail(T entity, float yaw, float tickDelta, MatrixStack matrices,
                                   VertexConsumerProvider consumers, int light, CallbackInfo ci) {
        if (!noether$chamsPushed) {
            return;
        }
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glPolygonOffset(1.0f, 1100000.0f);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        noether$chamsPushed = false;
    }
}
