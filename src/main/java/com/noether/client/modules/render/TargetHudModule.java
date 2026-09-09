package com.noether.client.modules.render;

import com.noether.client.config.ThemeManager;
import com.noether.client.core.TargetManager;
import com.noether.client.gui.hud.HudElement;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.render.RenderUtils;
import com.noether.client.util.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class TargetHudModule extends Module {
    private float displayedHp = 20f;

    public final HudElement hud = new HudElement("target", 300, 300) {
        @Override
        public void render(DrawContext context, float tickDelta) {
            if (!isVisible()) return;
            MinecraftClient client = MinecraftClient.getInstance();
            LivingEntity target = TargetManager.getCurrent();
            if (!(target instanceof PlayerEntity player) || client.player == null) {
                if (client.player != null && client.targetedEntity instanceof PlayerEntity p) {
                    player = p;
                    target = p;
                } else {
                    return;
                }
            }
            float hp = player.getHealth() + player.getAbsorptionAmount();
            displayedHp += (hp - displayedHp) * 0.18f;
            float max = Math.max(1f, player.getMaxHealth());
            float ratio = Math.min(1f, displayedHp / max);
            int dist = (int) client.player.distanceTo(player);

            setSize(140, 40);
            RenderUtils.roundRect(context, getX(), getY(), 140, 40, 4, 0xDD0B0614);
            context.fill(getX(), getY(), getX() + 3, getY() + 40, ThemeManager.accent());

            if (player instanceof AbstractClientPlayerEntity abs) {
                Identifier skin = abs.getSkinTextures().texture();
                context.drawTexture(skin, getX() + 8, getY() + 6, 8, 8, 8, 8, 64, 64);
            }

            context.drawText(client.textRenderer, player.getGameProfile().getName(), getX() + 32, getY() + 6, 0xFFF5F3FF, false);
            context.drawText(client.textRenderer, dist + "m  ·  armor " + player.getArmor(),
                    getX() + 32, getY() + 16, 0xFF9CA3AF, false);
            int barX = getX() + 32;
            int barY = getY() + 28;
            int barW = 98;
            context.fill(barX, barY, barX + barW, barY + 5, 0xFF1F2937);
            context.fill(barX, barY, barX + (int) (barW * ratio), barY + 5, ColorUtils.healthColor(ratio));
        }

        @Override
        public boolean isVisible() {
            return TargetHudModule.this.isEnabled();
        }
    };

    public TargetHudModule() {
        super("TargetHUD", "Панель текущей цели со скином и плавным HP", Category.RENDER);
        setEnabled(true);
    }
}
