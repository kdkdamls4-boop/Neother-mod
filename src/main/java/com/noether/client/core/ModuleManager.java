package com.noether.client.core;

import com.noether.client.gui.ClickGuiScreen;
import com.noether.client.gui.hud.NotificationManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.modules.combat.AutoClickerModule;
import com.noether.client.modules.combat.AutoTotemModule;
import com.noether.client.modules.combat.HitBoxesModule;
import com.noether.client.modules.combat.HitParticlesModule;
import com.noether.client.modules.combat.KillAuraModule;
import com.noether.client.modules.combat.ShieldBreakerModule;
import com.noether.client.modules.combat.TotemPopCounterModule;
import com.noether.client.modules.misc.MiddleClickFriendModule;
import com.noether.client.modules.misc.StaffAlertModule;
import com.noether.client.modules.movement.AutoSprintModule;
import com.noether.client.modules.movement.GuiMoveModule;
import com.noether.client.modules.movement.JumpCirclesModule;
import com.noether.client.modules.movement.SafeWalkModule;
import com.noether.client.modules.movement.TrailsModule;
import com.noether.client.modules.player.AutoRespawnModule;
import com.noether.client.modules.player.ChestStealerModule;
import com.noether.client.modules.player.FastPlaceModule;
import com.noether.client.modules.player.NoRenderModule;
import com.noether.client.modules.render.ArmorHudModule;
import com.noether.client.modules.render.BoxEspModule;
import com.noether.client.modules.render.ChamsModule;
import com.noether.client.modules.render.ChinaHatModule;
import com.noether.client.modules.render.CustomFogModule;
import com.noether.client.modules.render.CustomTimeModule;
import com.noether.client.modules.render.FreeCamModule;
import com.noether.client.modules.render.FullBrightModule;
import com.noether.client.modules.render.GlowEspModule;
import com.noether.client.modules.render.KeybindsListModule;
import com.noether.client.modules.render.NameTagsModule;
import com.noether.client.modules.render.PotionHudModule;
import com.noether.client.modules.render.StorageEspModule;
import com.noether.client.modules.render.TargetEspModule;
import com.noether.client.modules.render.TargetHudModule;
import com.noether.client.modules.render.TracersModule;
import com.noether.client.modules.render.ViewModelModule;
import com.noether.client.modules.render.WatermarkModule;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ModuleManager {
    private static final ModuleManager INSTANCE = new ModuleManager();
    private final List<Module> modules = new ArrayList<>();
    private final Set<Integer> pressed = new HashSet<>();
    private boolean clickGuiHeld;

    private ModuleManager() {}

    public static ModuleManager getInstance() {
        return INSTANCE;
    }

    public void init() {
        modules.clear();
        register(new AutoTotemModule());
        register(new AutoClickerModule());
        register(new ShieldBreakerModule());
        register(new KillAuraModule());
        register(new HitBoxesModule());
        register(new HitParticlesModule());
        register(new TotemPopCounterModule());
        register(new AutoSprintModule());
        register(new GuiMoveModule());
        register(new SafeWalkModule());
        register(new JumpCirclesModule());
        register(new TrailsModule());
        register(new ChamsModule());
        register(new GlowEspModule());
        register(new BoxEspModule());
        register(new TracersModule());
        register(new NameTagsModule());
        register(new TargetEspModule());
        register(new StorageEspModule());
        register(new TargetHudModule());
        register(new ChinaHatModule());
        register(new ViewModelModule());
        register(new CustomFogModule());
        register(new CustomTimeModule());
        register(new FreeCamModule());
        register(new FullBrightModule());
        register(new WatermarkModule());
        register(new ArmorHudModule());
        register(new PotionHudModule());
        register(new KeybindsListModule());
        register(new FastPlaceModule());
        register(new AutoRespawnModule());
        register(new NoRenderModule());
        register(new ChestStealerModule());
        register(new StaffAlertModule());
        register(new MiddleClickFriendModule());
    }

    private void register(Module module) {
        modules.add(module);
    }

    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public List<Module> byCategory(Category category) {
        return modules.stream().filter(m -> m.getCategory() == category).toList();
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T get(Class<T> type) {
        for (Module module : modules) {
            if (type.isInstance(module)) return (T) module;
        }
        return null;
    }

    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        handleKeys(client);
        GuiMoveModule move = get(GuiMoveModule.class);
        if (move != null) move.updateKeys();
        for (Module module : modules) {
            if (module.isEnabled()) module.onTick();
        }
    }

    private void handleKeys(MinecraftClient client) {
        long window = client.getWindow().getHandle();
        boolean guiDown = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
        if (guiDown && !clickGuiHeld) {
            if (client.currentScreen instanceof ClickGuiScreen) {
                client.setScreen(null);
            } else if (client.currentScreen == null) {
                client.setScreen(new ClickGuiScreen());
            }
        }
        clickGuiHeld = guiDown;

        if (client.currentScreen != null) {
            pressed.clear();
            return;
        }

        Set<Integer> now = new HashSet<>();
        for (Module module : modules) {
            int key = module.getKeybind();
            if (key <= 0) continue;
            boolean down = GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS;
            if (down) {
                now.add(key);
                if (!pressed.contains(key)) {
                    module.toggle();
                    NotificationManager.getInstance().moduleToggle(module.getName(), module.isEnabled());
                }
            }
        }
        pressed.clear();
        pressed.addAll(now);
    }
}
