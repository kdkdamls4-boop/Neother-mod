package com.noether.client.modules.render;

import com.noether.client.config.ThemeManager;
import com.noether.client.core.ModuleManager;
import com.noether.client.gui.hud.HudElement;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;

public class KeybindsListModule extends Module {
    public final HudElement hud = new HudElement("keybinds", 8, 32) {
        @Override
        public void render(DrawContext context, float tickDelta) {
            if (!isVisible()) return;
            MinecraftClient client = MinecraftClient.getInstance();
            int y = getY();
            int maxW = 70;
            context.drawText(client.textRenderer, "§dkeybinds", getX(), y, ThemeManager.accent(), false);
            y += 12;
            for (Module module : ModuleManager.getInstance().getModules()) {
                if (!module.isEnabled() || module.getKeybind() <= 0) continue;
                String key = InputUtil.fromKeyCode(module.getKeybind(), 0).getLocalizedText().getString();
                String line = module.getName() + " §7[§f" + key + "§7]";
                int w = client.textRenderer.getWidth(line);
                maxW = Math.max(maxW, w);
                context.drawText(client.textRenderer, line, getX(), y, 0xFFE5E7EB, false);
                y += 11;
            }
            setSize(maxW, Math.max(12, y - getY()));
        }

        @Override
        public boolean isVisible() {
            return KeybindsListModule.this.isEnabled();
        }
    };

    public KeybindsListModule() {
        super("KeybindsList", "Список активных горячих клавиш", Category.RENDER);
        setEnabled(true);
    }
}
