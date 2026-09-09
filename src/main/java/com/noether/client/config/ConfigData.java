package com.noether.client.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Plain JSON DTO. Independent from Minecraft so Gson tests can round-trip it.
 */
public class ConfigData {
    public String theme = "NEON_VIOLET";
    public int customColor = 0xFFA78BFA;
    public List<String> friends = new ArrayList<>();
    public Map<String, ModuleState> modules = new LinkedHashMap<>();
    public Map<String, HudPos> hud = new LinkedHashMap<>();

    public static class ModuleState {
        public boolean enabled;
        public int keybind = -1;
        public Map<String, Object> settings = new LinkedHashMap<>();
    }

    public static class HudPos {
        public int x;
        public int y;

        public HudPos() {}

        public HudPos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
