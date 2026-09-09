package com.noether.client.modules.render;

import com.noether.client.gui.hud.HudElement;
import com.noether.client.gui.hud.Notification;
import com.noether.client.gui.hud.NotificationManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ArmorHudModule extends Module {
    private long lastWarn;

    public final HudElement hud = new HudElement("armor", 8, 220) {
        @Override
        public void render(DrawContext context, float tickDelta) {
            if (!isVisible()) return;
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;
            int i = 0;
            for (ItemStack stack : client.player.getInventory().armor) {
                if (stack.isEmpty()) {
                    i++;
                    continue;
                }
                int y = getY() + (3 - i) * 18;
                context.drawItem(stack, getX(), y);
                if (stack.isDamageable()) {
                    float left = 1f - stack.getDamage() / (float) stack.getMaxDamage();
                    int pct = MathHelper.clamp((int) (left * 100), 0, 100);
                    int color = pct < 15 ? 0xFFEF4444 : pct < 40 ? 0xFFFBBF24 : 0xFF22C55E;
                    context.drawText(client.textRenderer, pct + "%", getX() + 18, y + 4, color, true);
                    if (pct < 15 && System.currentTimeMillis() - lastWarn > 8000) {
                        NotificationManager.getInstance().push("Armor", stack.getName().getString() + " · " + pct + "%",
                                Notification.Type.ERROR, 3200);
                        lastWarn = System.currentTimeMillis();
                    }
                }
                i++;
            }
            setSize(50, 72);
        }

        @Override
        public boolean isVisible() {
            return ArmorHudModule.this.isEnabled();
        }
    };

    public ArmorHudModule() {
        super("ArmorHUD", "Прочность надетой брони", Category.RENDER);
        setEnabled(true);
    }
}
