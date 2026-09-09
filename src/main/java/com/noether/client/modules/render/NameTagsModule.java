package com.noether.client.modules.render;

import com.noether.client.core.ModuleManager;
import com.noether.client.friend.FriendManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.modules.combat.TotemPopCounterModule;
import com.noether.client.render.RenderUtils;
import com.noether.client.settings.BooleanSetting;
import com.noether.client.util.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class NameTagsModule extends Module {
    private final BooleanSetting health = add(new BooleanSetting("Health", "Полоска HP", true));
    private final BooleanSetting armor = add(new BooleanSetting("Armor", "Броня", true));
    private final BooleanSetting pops = add(new BooleanSetting("Pops", "Тотем-попы", true));

    public NameTagsModule() {
        super("NameTags", "Кастомные плашки с HP, бронёй и зачарованиями", Category.RENDER);
    }

    public void render2D(DrawContext context) {
        if (!isEnabled()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null || client.gameRenderer == null) return;

        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        for (PlayerEntity player : client.world.getPlayers()) {
            if (player == client.player) continue;
            double[] pos = RenderUtils.worldToScreen(player.getX(), player.getY() + player.getHeight() + 0.4, player.getZ());
            if (pos == null) continue;
            int x = (int) pos[0];
            int y = (int) pos[1];
            if (x < 0 || y < 0 || x > sw || y > sh) continue;

            String name = player.getGameProfile().getName();
            boolean friend = FriendManager.getInstance().isFriend(name);
            float hp = player.getHealth() + player.getAbsorptionAmount();
            float max = player.getMaxHealth();
            float ratio = MathHelper.clamp(hp / Math.max(1f, max), 0, 1);

            String text = name + " " + String.format("%.0f", hp);
            if (pops.getBool()) {
                TotemPopCounterModule counter = ModuleManager.getInstance().get(TotemPopCounterModule.class);
                int n = counter == null ? 0 : counter.getPops(name);
                if (n > 0) text += " §d[" + n + "]";
            }
            int w = client.textRenderer.getWidth(text) + 10;
            int bx = x - w / 2;
            int by = y - 14;
            context.fill(bx, by, bx + w, by + 12, 0xAA0B0614);
            int nameColor = friend ? ColorUtils.friendGreen() : 0xFFFFFFFF;
            context.drawText(client.textRenderer, text, bx + 5, by + 2, nameColor, false);
            if (health.getBool()) {
                context.fill(bx, by + 12, bx + w, by + 14, 0xFF1F2937);
                context.fill(bx, by + 12, bx + (int) (w * ratio), by + 14, ColorUtils.healthColor(ratio));
            }
            if (armor.getBool() && player instanceof AbstractClientPlayerEntity) {
                int prot = player.getArmor();
                if (prot > 0) {
                    context.drawText(client.textRenderer, "✦" + prot, bx + w + 2, by + 2, 0xFF93C5FD, false);
                }
            }
        }
    }

    public void render(MatrixStack matrices) {
        // 2D pass only
    }
}
