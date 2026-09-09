package com.noether.client.modules.render;

import com.noether.client.config.ThemeManager;
import com.noether.client.gui.hud.HudElement;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class WatermarkModule extends Module {
    public final HudElement hud = new HudElement("watermark", 8, 8) {
        @Override
        public void render(DrawContext context, float tickDelta) {
            if (!isVisible()) return;
            MinecraftClient client = MinecraftClient.getInstance();
            int fps = client.getCurrentFps();
            int ping = 0;
            if (client.getNetworkHandler() != null && client.player != null) {
                var entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
                if (entry != null) ping = entry.getLatency();
            }
            String text = "NOETHER  §7|  §f" + fps + " fps  §7|  §f" + ping + " ms";
            int w = client.textRenderer.getWidth(text) + 16;
            setSize(w, 16);
            RenderUtils.roundRect(context, getX(), getY(), w, 16, 3, 0xCC0B0614);
            context.fill(getX(), getY(), getX() + 3, getY() + 16, ThemeManager.accent());
            context.drawText(client.textRenderer, text, getX() + 8, getY() + 4, 0xFFF5F3FF, false);
        }

        @Override
        public boolean isVisible() {
            return WatermarkModule.this.isEnabled();
        }
    };

    public WatermarkModule() {
        super("Watermark", "Плашка с логотипом, FPS и пингом", Category.RENDER);
        setEnabled(true);
    }
}
