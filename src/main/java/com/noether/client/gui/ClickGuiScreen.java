package com.noether.client.gui;

import com.noether.client.config.ThemeManager;
import com.noether.client.core.ModuleManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.render.RenderUtils;
import com.noether.client.settings.BooleanSetting;
import com.noether.client.settings.ColorSetting;
import com.noether.client.settings.ModeSetting;
import com.noether.client.settings.NumberSetting;
import com.noether.client.settings.Setting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClickGuiScreen extends Screen {
    private final Map<Category, Panel> panels = new EnumMap<>(Category.class);
    private final Set<Module> expanded = new HashSet<>();
    private Module binding;
    private NumberSetting sliding;
    private long openTime;

    public ClickGuiScreen() {
        super(Text.literal("Noether"));
        int x = 18;
        for (Category cat : Category.values()) {
            panels.put(cat, new Panel(cat, x, 28));
            x += 118;
        }
        openTime = System.currentTimeMillis();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x66100918);
        float appear = Math.min(1f, (System.currentTimeMillis() - openTime) / 180f);
        context.drawText(textRenderer, "NOETHER  §7client  ·  " + ThemeManager.getTheme().getDisplay(),
                18, 10, ThemeManager.accent(), false);
        for (Panel panel : panels.values()) {
            panel.render(context, mouseX, mouseY, appear);
        }
        if (binding != null) {
            String msg = "Press a key to bind §f" + binding.getName() + " §7(DEL to unbind)";
            int w = textRenderer.getWidth(msg);
            context.fill(width / 2 - w / 2 - 8, height - 32, width / 2 + w / 2 + 8, height - 14, 0xEE0B0614);
            context.drawText(textRenderer, msg, width / 2 - w / 2, height - 26, ThemeManager.accent(), false);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Panel panel : panels.values()) {
            if (panel.mouseClicked(mouseX, mouseY, button)) return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        sliding = null;
        for (Panel panel : panels.values()) panel.dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        for (Panel panel : panels.values()) {
            if (panel.dragging) {
                panel.x += (int) dx;
                panel.y += (int) dy;
                return true;
            }
        }
        if (sliding != null) {
            applySlider(sliding, mouseX);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (binding != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                binding = null;
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_DELETE || keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                binding.setKeybind(-1);
            } else {
                binding.setKeybind(keyCode);
            }
            binding = null;
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void applySlider(NumberSetting setting, double mouseX) {
        // filled by panel when it knows the slider rect; fallback no-op
    }

    private class Panel {
        final Category category;
        int x, y;
        boolean dragging;
        int scroll;

        Panel(Category category, int x, int y) {
            this.category = category;
            this.x = x;
            this.y = y;
        }

        void render(DrawContext context, int mouseX, int mouseY, float appear) {
            var mods = ModuleManager.getInstance().byCategory(category);
            int h = 22;
            for (Module m : mods) {
                h += 16;
                if (expanded.contains(m)) h += m.getSettings().size() * 14 + 14;
            }
            h = (int) (h * appear);
            RenderUtils.roundRect(context, x, y, 110, Math.max(22, h), 4, 0xE00B0614);
            context.fill(x, y, x + 110, y + 18, ThemeManager.accentDark());
            context.drawText(textRenderer, category.getDisplay(), x + 8, y + 5, 0xFFF5F3FF, false);
            int cy = y + 20;
            RenderUtils.scissor(x, y + 20, 110, Math.max(0, h - 20));
            for (Module m : mods) {
                int bg = m.isEnabled() ? 0x3322C55E : 0x22000000;
                context.fill(x + 2, cy, x + 108, cy + 15, bg);
                int col = m.isEnabled() ? ThemeManager.accent() : 0xFF9CA3AF;
                context.drawText(textRenderer, m.getName(), x + 6, cy + 4, col, false);
                if (!m.getSettings().isEmpty()) {
                    context.drawText(textRenderer, expanded.contains(m) ? "-" : "+", x + 98, cy + 4, 0xFF6B7280, false);
                }
                cy += 16;
                if (expanded.contains(m)) {
                    for (Setting<?> s : m.getSettings()) {
                        drawSetting(context, s, cy);
                        cy += 14;
                    }
                    String bind = m.getKeybind() > 0
                            ? net.minecraft.client.util.InputUtil.fromKeyCode(m.getKeybind(), 0).getLocalizedText().getString()
                            : "none";
                    context.drawText(textRenderer, "Bind: " + bind, x + 8, cy + 2, 0xFFD1D5DB, false);
                    cy += 14;
                }
            }
            RenderUtils.disableScissor();
        }

        void drawSetting(DrawContext context, Setting<?> s, int cy) {
            if (s instanceof BooleanSetting b) {
                context.drawText(textRenderer, s.getName(), x + 8, cy + 3, 0xFFD1D5DB, false);
                int box = b.getBool() ? ThemeManager.accent() : 0xFF374151;
                context.fill(x + 92, cy + 3, x + 102, cy + 12, box);
            } else if (s instanceof NumberSetting n) {
                context.drawText(textRenderer, s.getName() + " §7" + trim(n.get()), x + 8, cy + 1, 0xFFD1D5DB, false);
                context.fill(x + 8, cy + 11, x + 102, cy + 13, 0xFF1F2937);
                double t = (n.get() - n.getMin()) / (n.getMax() - n.getMin());
                context.fill(x + 8, cy + 11, x + 8 + (int) (94 * t), cy + 13, ThemeManager.accent());
            } else if (s instanceof ModeSetting m) {
                context.drawText(textRenderer, s.getName() + " §7" + m.get(), x + 8, cy + 3, 0xFFD1D5DB, false);
            } else if (s instanceof ColorSetting c) {
                context.drawText(textRenderer, s.getName(), x + 8, cy + 3, 0xFFD1D5DB, false);
                context.fill(x + 92, cy + 3, x + 102, cy + 12, c.getRgb());
            }
        }

        boolean mouseClicked(double mx, double my, int button) {
            if (mx >= x && mx <= x + 110 && my >= y && my <= y + 18) {
                if (button == 0) {
                    dragging = true;
                    return true;
                }
                if (button == 1) {
                    ThemeManager.cycle();
                    return true;
                }
            }
            var mods = ModuleManager.getInstance().byCategory(category);
            int cy = y + 20;
            for (Module m : mods) {
                if (mx >= x + 2 && mx <= x + 108 && my >= cy && my <= cy + 15) {
                    if (button == 0) {
                        m.toggle();
                        com.noether.client.gui.hud.NotificationManager.getInstance()
                                .moduleToggle(m.getName(), m.isEnabled());
                    } else if (button == 1) {
                        if (expanded.contains(m)) expanded.remove(m);
                        else expanded.add(m);
                    }
                    return true;
                }
                cy += 16;
                if (expanded.contains(m)) {
                    for (Setting<?> s : m.getSettings()) {
                        if (mx >= x + 6 && mx <= x + 104 && my >= cy && my <= cy + 14) {
                            clickSetting(s, mx, button);
                            return true;
                        }
                        cy += 14;
                    }
                    if (mx >= x + 6 && mx <= x + 104 && my >= cy && my <= cy + 14) {
                        binding = m;
                        return true;
                    }
                    cy += 14;
                }
            }
            return false;
        }

        void clickSetting(Setting<?> s, double mx, int button) {
            if (s instanceof BooleanSetting b) b.toggle();
            else if (s instanceof ModeSetting m) m.cycle();
            else if (s instanceof NumberSetting n) {
                sliding = n;
                double t = (mx - (x + 8)) / 94.0;
                t = Math.max(0, Math.min(1, t));
                n.set(n.getMin() + t * (n.getMax() - n.getMin()));
            } else if (s instanceof ColorSetting c && button == 1) {
                ThemeManager.setCustomColor(c.getRgb());
                ThemeManager.setTheme(ThemeManager.Theme.CUSTOM);
            }
        }
    }

    private static String trim(double v) {
        if (Math.abs(v - Math.round(v)) < 0.001) return String.valueOf(Math.round(v));
        return String.format("%.1f", v);
    }
}
