package com.noether.client.modules.movement;

import com.noether.client.gui.ClickGuiScreen;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class GuiMoveModule extends Module {
    public GuiMoveModule() {
        super("GuiMove", "Движение с открытым инвентарём / GUI", Category.MOVEMENT);
    }

    public void updateKeys() {
        if (!isEnabled()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.currentScreen == null) return;
        if (client.currentScreen instanceof ChatScreen) return;
        if (client.currentScreen instanceof ClickGuiScreen
                || client.currentScreen instanceof InventoryScreen
                || client.currentScreen instanceof HandledScreen<?>) {
            long window = client.getWindow().getHandle();
            set(client.options.forwardKey, window, GLFW.GLFW_KEY_W);
            set(client.options.backKey, window, GLFW.GLFW_KEY_S);
            set(client.options.leftKey, window, GLFW.GLFW_KEY_A);
            set(client.options.rightKey, window, GLFW.GLFW_KEY_D);
            set(client.options.jumpKey, window, GLFW.GLFW_KEY_SPACE);
            set(client.options.sprintKey, window, GLFW.GLFW_KEY_LEFT_CONTROL);
        }
    }

    private void set(KeyBinding bind, long window, int code) {
        bind.setPressed(GLFW.glfwGetKey(window, code) == GLFW.GLFW_PRESS);
    }
}
