package com.noether.client.gui.hud;

import com.noether.client.core.ModuleManager;
import com.noether.client.modules.render.ArmorHudModule;
import com.noether.client.modules.render.KeybindsListModule;
import com.noether.client.modules.render.PotionHudModule;
import com.noether.client.modules.render.TargetHudModule;
import com.noether.client.modules.render.WatermarkModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;

import java.util.ArrayList;
import java.util.List;

public final class HudManager {
    private static final HudManager INSTANCE = new HudManager();
    private final List<HudElement> elements = new ArrayList<>();
    private HudElement dragging;

    private HudManager() {}

    public static HudManager getInstance() {
        return INSTANCE;
    }

    public void init() {
        elements.clear();
        ModuleManager mm = ModuleManager.getInstance();
        add(mm.get(WatermarkModule.class).hud);
        add(mm.get(KeybindsListModule.class).hud);
        add(mm.get(PotionHudModule.class).hud);
        add(mm.get(ArmorHudModule.class).hud);
        add(mm.get(TargetHudModule.class).hud);
    }

    private void add(HudElement element) {
        if (element != null) elements.add(element);
    }

    public List<HudElement> getElements() {
        return elements;
    }

    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        boolean edit = client.currentScreen instanceof ChatScreen;
        NotificationManager.getInstance().update();
        for (HudElement el : elements) {
            if (!el.isVisible() && !edit) continue;
            el.render(context, tickDelta);
            if (edit) {
                context.fill(el.getX() - 1, el.getY() - 1, el.getX() + el.getWidth() + 1, el.getY() + el.getHeight() + 1, 0x55A78BFA);
            }
        }
        renderNotifications(context, client);
    }

    private void renderNotifications(DrawContext context, MinecraftClient client) {
        int y = 40;
        int sw = client.getWindow().getScaledWidth();
        long now = System.currentTimeMillis();
        for (Notification n : NotificationManager.getInstance().getActive()) {
            float a = n.animAlpha(now);
            int alpha = (int) (a * 230);
            int w = Math.max(110, client.textRenderer.getWidth(n.getMessage()) + 24);
            int x = sw - w - 10 + (int) (n.slide(now) * 80);
            int bg = (alpha << 24) | 0x0B0614;
            context.fill(x, y, x + w, y + 28, bg);
            int accent = switch (n.getType()) {
                case SUCCESS -> 0xFF22C55E;
                case WARNING -> 0xFFFBBF24;
                case ERROR -> 0xFFEF4444;
                case MODULE -> 0xFFA78BFA;
                default -> 0xFF38BDF8;
            };
            context.fill(x, y, x + 3, y + 28, accent);
            context.drawText(client.textRenderer, n.getTitle(), x + 8, y + 4, 0xFFF5F3FF, false);
            context.drawText(client.textRenderer, n.getMessage(), x + 8, y + 15, 0xFFD1D5DB, false);
            y += 32;
        }
    }

    public boolean mouseClicked(double x, double y) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!(client.currentScreen instanceof ChatScreen)) return false;
        for (int i = elements.size() - 1; i >= 0; i--) {
            HudElement el = elements.get(i);
            if (el.isMouseOver(x, y)) {
                el.startDrag(x, y);
                dragging = el;
                return true;
            }
        }
        return false;
    }

    public void mouseDragged(double x, double y) {
        if (dragging != null) dragging.dragTo(x, y);
    }

    public void mouseReleased() {
        if (dragging != null) {
            dragging.stopDrag();
            dragging = null;
        }
    }
}
