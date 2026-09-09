package com.noether.client.modules.misc;

import com.noether.client.gui.hud.Notification;
import com.noether.client.gui.hud.NotificationManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.BooleanSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameMode;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public class StaffAlertModule extends Module {
    private final BooleanSetting tab = add(new BooleanSetting("Tab List", "Паттерны в табе", true));
    private final BooleanSetting spectators = add(new BooleanSetting("Spectators", "Невидимые наблюдатели рядом", true));

    private static final Pattern STAFF = Pattern.compile(
            ".*(staff|moderator|moder|admin|curator|helper|ютубер|админ|стафф|куратор|хелпер).*",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );

    private final Set<String> alerted = new HashSet<>();

    public StaffAlertModule() {
        super("StaffAlert", "Детект персонала сервера и невидимых спектаторов", Category.MISC);
    }

    @Override
    public void onDisable() {
        alerted.clear();
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.getNetworkHandler() == null) return;

        if (tab.getBool()) {
            for (PlayerListEntry entry : client.getNetworkHandler().getPlayerList()) {
                String name = entry.getProfile().getName();
                String display = entry.getDisplayName() != null ? entry.getDisplayName().getString() : name;
                String blob = (name + " " + display).toLowerCase(Locale.ROOT);
                if (STAFF.matcher(blob).matches() || looksPrefixed(display)) {
                    ping(name, "Staff in tab");
                }
            }
        }

        if (spectators.getBool() && client.world != null) {
            for (PlayerEntity player : client.world.getPlayers()) {
                if (player == client.player) continue;
                if (player.isSpectator() || player.getAbilities().flying && player.isInvisible()) {
                    ping(player.getGameProfile().getName(), "Spectator nearby");
                }
                if (entryGameMode(client, player) == GameMode.SPECTATOR) {
                    ping(player.getGameProfile().getName(), "Spectator GM");
                }
            }
        }
    }

    private GameMode entryGameMode(MinecraftClient client, PlayerEntity player) {
        PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(player.getUuid());
        return entry == null ? null : entry.getGameMode();
    }

    private boolean looksPrefixed(String display) {
        String d = display.toLowerCase(Locale.ROOT);
        return d.contains("[staff]") || d.contains("[moder]") || d.contains("[admin]")
                || d.contains("[curator]") || d.contains("[helper]") || d.contains("[yt]");
    }

    private void ping(String name, String reason) {
        if (!alerted.add(name.toLowerCase(Locale.ROOT))) return;
        NotificationManager.getInstance().push("StaffAlert", name + " · " + reason, Notification.Type.ERROR, 5000);
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.playSound(net.minecraft.sound.SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), 1.0f, 0.7f);
        }
    }
}
