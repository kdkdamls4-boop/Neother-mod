package com.noether.client.modules.combat;

import com.noether.client.core.RotationManager;
import com.noether.client.core.TargetManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.BooleanSetting;
import com.noether.client.settings.ModeSetting;
import com.noether.client.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

public class KillAuraModule extends Module {
    private final NumberSetting range = add(new NumberSetting("Range", "Дальность атаки", 4.2, 2.5, 6.0, 0.1));
    private final ModeSetting priority = add(new ModeSetting("Priority", "Сортировка целей", "Distance", "Distance", "Health", "Angle"));
    private final BooleanSetting silent = add(new BooleanSetting("Silent", "Ротации только в пакетах", true));
    private final BooleanSetting playersOnly = add(new BooleanSetting("Players", "Только игроки", true));
    private final BooleanSetting walls = add(new BooleanSetting("Through Walls", "Бить сквозь стены", false));
    private final BooleanSetting cooldown = add(new BooleanSetting("1.9 Cooldown", "Синхронизация кулдауна", true));
    private final BooleanSetting swing = add(new BooleanSetting("Swing", "Анимация руки", true));

    public KillAuraModule() {
        super("KillAura", "Silent-аура с Raytrace, кулдауном 1.9+ и игнором друзей", Category.COMBAT);
    }

    @Override
    public void onDisable() {
        RotationManager.getInstance().clear();
        TargetManager.clear();
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null || client.interactionManager == null) return;
        if (client.player.isSpectator() || client.player.isDead()) {
            RotationManager.getInstance().clear();
            return;
        }

        TargetManager.Priority prio = switch (priority.get().toLowerCase()) {
            case "health" -> TargetManager.Priority.HEALTH;
            case "angle" -> TargetManager.Priority.ANGLE;
            default -> TargetManager.Priority.DISTANCE;
        };

        LivingEntity target = TargetManager.find(client, range.get(), prio, playersOnly.getBool(), !walls.getBool());
        if (target == null) {
            RotationManager.getInstance().clear();
            return;
        }

        float[] rot = RotationManager.lookAt(
                client.player.getX(), client.player.getEyeY(), client.player.getZ(),
                target.getX(), target.getEyeY(), target.getZ()
        );

        if (silent.getBool()) {
            RotationManager.getInstance().set(rot[0], rot[1]);
        } else {
            client.player.setYaw(rot[0]);
            client.player.setPitch(rot[1]);
            RotationManager.getInstance().clear();
        }

        if (cooldown.getBool() && client.player.getAttackCooldownProgress(0.5f) < 1.0f) {
            return;
        }

        client.interactionManager.attackEntity(client.player, target);
        if (swing.getBool()) {
            client.player.swingHand(Hand.MAIN_HAND);
        }
    }
}
