package com.noether.client.modules.combat;

import com.noether.client.friend.FriendManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.BooleanSetting;
import com.noether.client.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class AutoClickerModule extends Module {
    private final BooleanSetting weaponsOnly = add(new BooleanSetting("Weapons Only", "Только мечи / топоры", true));
    private final BooleanSetting cooldown = add(new BooleanSetting("1.9 Cooldown", "Ждать полный кулдаун", true));
    private final NumberSetting cps = add(new NumberSetting("CPS", "Клики в секунду если кулдаун выкл", 12, 1, 20, 1));
    private final BooleanSetting skipFriends = add(new BooleanSetting("Skip Friends", "Не бить друзей", true));

    private long lastClick;

    public AutoClickerModule() {
        super("AutoClicker", "Автокликер с кулдауном 1.9+ и пропуском друзей", Category.COMBAT);
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.interactionManager == null || client.world == null) return;
        if (client.currentScreen != null) return;
        if (!client.options.attackKey.isPressed()) return;
        if (weaponsOnly.getBool() && !isWeapon(client)) return;

        HitResult hit = client.crosshairTarget;
        if (!(hit instanceof EntityHitResult entityHit)) return;
        Entity target = entityHit.getEntity();
        if (skipFriends.getBool() && target instanceof PlayerEntity player
                && FriendManager.getInstance().isFriend(player.getGameProfile().getName())) {
            return;
        }

        if (cooldown.getBool()) {
            if (client.player.getAttackCooldownProgress(0.5f) < 1.0f) return;
            doAttack(client);
            return;
        }

        long now = System.currentTimeMillis();
        long delay = (long) (1000.0 / Math.max(1.0, cps.get()));
        if (now - lastClick >= delay) {
            doAttack(client);
            lastClick = now;
        }
    }

    private void doAttack(MinecraftClient client) {
        if (client.crosshairTarget instanceof EntityHitResult hit && hit.getEntity() != null) {
            client.interactionManager.attackEntity(client.player, hit.getEntity());
            client.player.swingHand(client.player.getActiveHand());
        }
    }

    private boolean isWeapon(MinecraftClient client) {
        String key = client.player.getMainHandStack().getItem().toString().toLowerCase();
        return key.contains("sword") || key.contains("axe") || key.contains("trident") || key.contains("mace");
    }
}
