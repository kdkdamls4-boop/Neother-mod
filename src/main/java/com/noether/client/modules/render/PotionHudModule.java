package com.noether.client.modules.render;

import com.noether.client.config.ThemeManager;
import com.noether.client.gui.hud.HudElement;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffectInstance;

public class PotionHudModule extends Module {
    public final HudElement hud = new HudElement("potion", 8, 120) {
        @Override
        public void render(DrawContext context, float tickDelta) {
            if (!isVisible()) return;
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;
            int y = getY();
            int maxW = 40;
            for (StatusEffectInstance effect : client.player.getStatusEffects()) {
                String name = effect.getEffectType().getName().getString();
                int amp = effect.getAmplifier();
                String ampStr = amp > 0 ? " " + (amp + 1) : "";
                int sec = effect.getDuration() / 20;
                String time = String.format("%d:%02d", sec / 60, sec % 60);
                String line = name + ampStr + " §7" + time;
                int w = client.textRenderer.getWidth(line) + 8;
                maxW = Math.max(maxW, w);
                context.fill(getX(), y, getX() + w, y + 12, 0x990B0614);
                context.fill(getX(), y, getX() + 2, y + 12, ThemeManager.accent());
                context.drawText(client.textRenderer, line, getX() + 5, y + 2, 0xFFF5F3FF, false);
                y += 13;
            }
            setSize(maxW, Math.max(12, y - getY()));
        }

        @Override
        public boolean isVisible() {
            return PotionHudModule.this.isEnabled();
        }
    };

    public PotionHudModule() {
        super("PotionHUD", "Активные зелья с таймерами MM:SS", Category.RENDER);
        setEnabled(true);
    }
}
