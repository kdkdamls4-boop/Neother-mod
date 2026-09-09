package com.noether.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.noether.client.core.ModuleManager;
import com.noether.client.friend.FriendManager;
import com.noether.client.gui.hud.HudElement;
import com.noether.client.gui.hud.HudManager;
import com.noether.client.modules.Module;
import com.noether.client.settings.Setting;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private Path path;

    public ConfigManager(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public ConfigData capture() {
        ConfigData data = new ConfigData();
        data.theme = ThemeManager.getTheme().name();
        data.customColor = ThemeManager.getCustomColor();
        data.friends.addAll(FriendManager.getInstance().list());

        if (ModuleManager.getInstance() != null) {
            for (Module module : ModuleManager.getInstance().getModules()) {
                ConfigData.ModuleState state = new ConfigData.ModuleState();
                state.enabled = module.isEnabled();
                state.keybind = module.getKeybind();
                for (Setting<?> setting : module.getSettings()) {
                    state.settings.put(setting.getName(), setting.toConfig());
                }
                data.modules.put(module.getName(), state);
            }
        }

        if (HudManager.getInstance() != null) {
            for (HudElement element : HudManager.getInstance().getElements()) {
                data.hud.put(element.getId(), new ConfigData.HudPos(element.getX(), element.getY()));
            }
        }
        return data;
    }

    public void apply(ConfigData data) {
        if (data == null) return;
        ThemeManager.setTheme(data.theme);
        ThemeManager.setCustomColor(data.customColor);
        FriendManager.getInstance().replaceAll(data.friends);

        if (ModuleManager.getInstance() != null && data.modules != null) {
            for (Module module : ModuleManager.getInstance().getModules()) {
                ConfigData.ModuleState state = data.modules.get(module.getName());
                if (state == null) continue;
                module.setKeybind(state.keybind);
                module.setEnabled(state.enabled);
                if (state.settings == null) continue;
                for (Setting<?> setting : module.getSettings()) {
                    Object raw = state.settings.get(setting.getName());
                    if (raw != null) {
                        setting.fromConfig(raw);
                    }
                }
            }
        }

        if (HudManager.getInstance() != null && data.hud != null) {
            for (HudElement element : HudManager.getInstance().getElements()) {
                ConfigData.HudPos pos = data.hud.get(element.getId());
                if (pos != null) {
                    element.setPosition(pos.x, pos.y);
                }
            }
        }
    }

    public void save() throws IOException {
        save(capture());
    }

    public void save(ConfigData data) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(path, GSON.toJson(data), StandardCharsets.UTF_8);
    }

    public ConfigData load() throws IOException {
        if (!Files.exists(path)) {
            return new ConfigData();
        }
        String json = Files.readString(path, StandardCharsets.UTF_8);
        ConfigData data = GSON.fromJson(json, ConfigData.class);
        return data == null ? new ConfigData() : data;
    }

    public void loadAndApply() throws IOException {
        apply(load());
    }

    public static Map<String, Object> jsonStructureSample() {
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("theme", "NEON_VIOLET");
        root.put("customColor", 0xFFA78BFA);
        root.put("friends", java.util.List.of());
        root.put("modules", new LinkedHashMap<>());
        root.put("hud", new LinkedHashMap<>());
        return root;
    }
}
