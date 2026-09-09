package com.noether.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.noether.client.core.RotationManager;
import com.noether.client.config.ConfigData;
import com.noether.client.config.ConfigManager;
import com.noether.client.config.ThemeManager;
import com.noether.client.friend.FriendCommands;
import com.noether.client.friend.FriendManager;
import com.noether.client.gui.hud.Notification;
import com.noether.client.gui.hud.NotificationManager;
import com.noether.client.util.ColorUtils;
import com.noether.client.util.SlotMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

public class VisualsCoreTest {
    @BeforeEach
    void reset() {
        FriendManager.getInstance().clear();
        NotificationManager.getInstance().clear();
        NotificationManager.getInstance().setClock(System::currentTimeMillis);
        ThemeManager.setTheme(ThemeManager.Theme.NEON_VIOLET);
        ThemeManager.setCustomColor(0xFFA78BFA);
    }

    @AfterEach
    void cleanup() {
        FriendManager.getInstance().clear();
        NotificationManager.getInstance().clear();
        NotificationManager.getInstance().setClock(System::currentTimeMillis);
    }

    @Test
    void testFriendManagerBasics() {
        FriendManager fm = FriendManager.getInstance();
        assertTrue(fm.add("Steve"));
        assertFalse(fm.add("steve"));
        assertTrue(fm.isFriend("STEVE"));
        assertTrue(fm.remove("sTeVe"));
        assertFalse(fm.isFriend("Steve"));
        fm.add("Alex");
        fm.add("Notch");
        assertEquals(2, fm.list().size());
        assertFalse(fm.toggle("Alex"));
        assertFalse(fm.isFriend("Alex"));
        assertEquals(1, fm.list().size());
        fm.clear();
        assertTrue(fm.toggle("Herobrine"));
        assertTrue(fm.isFriend("Herobrine"));
        assertFalse(fm.toggle("Herobrine"));
        assertTrue(fm.list().isEmpty());
    }

    @Test
    void testFriendChatCommands() {
        FriendManager fm = FriendManager.getInstance();
        assertTrue(FriendCommands.isFriendCommand("/friend add Steve"));
        assertTrue(FriendCommands.isFriendCommand("/FRIEND list"));
        assertFalse(FriendCommands.isFriendCommand("/help"));

        String add = FriendCommands.handle("/friend add Steve", fm);
        assertTrue(add.toLowerCase().contains("steve"));
        assertTrue(fm.isFriend("Steve"));

        String dup = FriendCommands.handle("/FRIEND ADD steve", fm);
        assertTrue(dup.toLowerCase().contains("уже") || dup.toLowerCase().contains("steve"));

        String list = FriendCommands.handle("/friend list", fm);
        assertTrue(list.contains("Steve"));

        String rm = FriendCommands.handle("/friend remove Steve", fm);
        assertFalse(fm.isFriend("Steve"));
        assertTrue(rm.toLowerCase().contains("steve"));

        FriendCommands.handle("/friend add Alex", fm);
        FriendCommands.handle("/friend clear", fm);
        assertTrue(fm.list().isEmpty());

        String usage = FriendCommands.handle("/friend", fm);
        assertTrue(usage.contains("/friend"));
    }

    @Test
    void testChamsColorLogic() {
        int custom = 0xFFAA00FF;
        int waveA = 0xFFFF0000;
        int waveB = 0xFF0000FF;
        int friend = ColorUtils.chamsColor(custom, waveA, waveB, 0, true, 0, 0);
        assertEquals(ColorUtils.friendGreen(), friend);

        int customOut = ColorUtils.chamsColor(custom, waveA, waveB, 0, false, 0, 0);
        assertEquals(custom, customOut);

        int rainbow = ColorUtils.chamsColor(custom, waveA, waveB, 1, false, 0, 0);
        assertNotEquals(0, rainbow);

        int wave0 = ColorUtils.chamsColor(custom, waveA, waveB, 2, false, 0, 0);
        int wave1 = ColorUtils.chamsColor(custom, waveA, waveB, 2, false, 0, 1500);
        assertNotEquals(wave0, wave1);

        int mid = ColorUtils.interpolate(0xFF000000, 0xFFFFFFFF, 0.5f);
        assertEquals(127, ColorUtils.red(mid), 1);
        int hpFull = ColorUtils.healthColor(1f);
        int hpDead = ColorUtils.healthColor(0f);
        assertTrue(ColorUtils.green(hpFull) > ColorUtils.green(hpDead));
    }

    @Test
    void testRealConfigManagerSaveAndLoad() throws Exception {
        Path tmp = Files.createTempFile("noether-config", ".json");
        try {
            ConfigManager manager = new ConfigManager(tmp);
            ConfigData data = new ConfigData();
            data.theme = "CYBER_CYAN";
            data.customColor = 0xFF112233;
            data.friends.add("Steve");
            data.friends.add("Alex");
            ConfigData.ModuleState aura = new ConfigData.ModuleState();
            aura.enabled = true;
            aura.keybind = 82;
            aura.settings.put("Range", 4.5);
            aura.settings.put("Silent", true);
            data.modules.put("KillAura", aura);
            data.hud.put("watermark", new ConfigData.HudPos(12, 16));
            manager.save(data);

            ConfigData loaded = manager.load();
            assertEquals("CYBER_CYAN", loaded.theme);
            assertEquals(0xFF112233, loaded.customColor);
            assertTrue(loaded.friends.contains("Steve"));
            assertTrue(loaded.modules.get("KillAura").enabled);
            assertEquals(82, loaded.modules.get("KillAura").keybind);
            assertEquals(12, loaded.hud.get("watermark").x);
            assertEquals(16, loaded.hud.get("watermark").y);
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    void testThemeManager() {
        ThemeManager.setTheme(ThemeManager.Theme.NEON_VIOLET);
        assertEquals(ThemeManager.Theme.NEON_VIOLET, ThemeManager.getTheme());
        assertEquals(0xFFA78BFA, ThemeManager.accent());
        ThemeManager.setTheme("Cyber Cyan");
        assertEquals(ThemeManager.Theme.CYBER_CYAN, ThemeManager.getTheme());
        ThemeManager.setTheme("RAINBOW");
        int a = ThemeManager.accent(0);
        int b = ThemeManager.accent(0.5f);
        assertNotEquals(a, b);
        ThemeManager.setCustomColor(0xFF123456);
        ThemeManager.setTheme(ThemeManager.Theme.CUSTOM);
        assertEquals(0xFF123456, ThemeManager.accent());
        ThemeManager.cycle();
        assertNotNull(ThemeManager.getTheme());
    }

    @Test
    void testColorUtilsWaveAndInterpolate() {
        int c1 = 0xFFFF0000;
        int c2 = 0xFF0000FF;
        int mid = ColorUtils.interpolate(c1, c2, 0.5f);
        assertEquals(127, ColorUtils.red(mid), 2);
        assertEquals(0, ColorUtils.green(mid), 2);
        assertEquals(127, ColorUtils.blue(mid), 2);

        int w0 = ColorUtils.wave(c1, c2, 2.0f, 0);
        int w1 = ColorUtils.wave(c1, c2, 2.0f, 800);
        assertNotEquals(w0, w1);

        int rb = ColorUtils.rainbow(0, 0);
        assertEquals(255, ColorUtils.alpha(rb));
        int faded = ColorUtils.withAlpha(0x00FFFFFF, 64);
        assertEquals(64, ColorUtils.alpha(faded));
    }

    @Test
    void testNotificationLifecycle() {
        AtomicLong now = new AtomicLong(1_000_000);
        NotificationManager nm = NotificationManager.getInstance();
        nm.setClock(now::get);
        nm.push("KillAura", "Enabled", Notification.Type.SUCCESS, 1000);
        nm.moduleToggle("AutoTotem", true);
        nm.friend("Steve", true);
        nm.update(now.get());
        assertEquals(3, nm.getActive().size());
        Notification first = nm.getActive().get(0);
        assertEquals(0f, first.slide(now.get() + 200), 0.01f);
        assertTrue(first.animAlpha(now.get()) < 1f);
        now.addAndGet(5000);
        nm.update(now.get());
        assertTrue(nm.getActive().isEmpty());
    }

    @Test
    void testSilentRotationSendGate() {
        RotationManager rot = RotationManager.getInstance();
        rot.clear();
        java.util.UUID id = java.util.UUID.randomUUID();
        rot.set(90f, 10f, id);
        assertFalse(rot.hasBeenSent());
        rot.markSent();
        assertTrue(rot.hasBeenSent());
        rot.set(91f, 11f, id);
        assertTrue(rot.hasBeenSent());
        rot.set(0f, 0f, java.util.UUID.randomUUID());
        assertFalse(rot.hasBeenSent());
        rot.clear();
        assertFalse(rot.isActive());
        assertFalse(rot.hasBeenSent());
    }

    @Test
    void testInventorySlotMap() {
        assertEquals(36, SlotMap.inventoryToHandler(0));
        assertEquals(44, SlotMap.inventoryToHandler(8));
        assertEquals(9, SlotMap.inventoryToHandler(9));
        assertEquals(35, SlotMap.inventoryToHandler(35));
        assertEquals(8, SlotMap.inventoryToHandler(36));
        assertEquals(7, SlotMap.inventoryToHandler(37));
        assertEquals(6, SlotMap.inventoryToHandler(38));
        assertEquals(5, SlotMap.inventoryToHandler(39));
        assertEquals(45, SlotMap.inventoryToHandler(40));
        assertEquals(-1, SlotMap.inventoryToHandler(41));
        assertEquals(-1, SlotMap.inventoryToHandler(-1));
    }

    @Test
    void testConfigJsonStructure() {
        Map<String, Object> sample = ConfigManager.jsonStructureSample();
        assertTrue(sample.containsKey("theme"));
        assertTrue(sample.containsKey("customColor"));
        assertTrue(sample.containsKey("friends"));
        assertTrue(sample.containsKey("modules"));
        assertTrue(sample.containsKey("hud"));

        ConfigData data = new ConfigData();
        data.theme = "SUNSET_ORANGE";
        data.friends.add("Notch");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(data);
        ConfigData back = gson.fromJson(json, ConfigData.class);
        assertEquals("SUNSET_ORANGE", back.theme);
        assertEquals("Notch", back.friends.get(0));
        assertNotNull(back.modules);
        assertNotNull(back.hud);
        assertTrue(json.contains("\"theme\""));
        assertTrue(json.contains("\"friends\""));
        assertTrue(json.contains("\"modules\""));
        assertTrue(json.contains("\"hud\""));
    }
}
