package com.noether.client.modules.combat;

import com.noether.client.core.InventoryUtils;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;

public class ShieldBreakerModule extends Module {
    private final NumberSetting delay = add(new NumberSetting("Switch Back", "Тики до возврата слота", 4, 1, 20, 1));

    private int returnSlot = -1;
    private int ticks;

    public ShieldBreakerModule() {
        super("ShieldBreaker", "Авто-свап на топор при блоке щитом", Category.COMBAT);
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.interactionManager == null || client.getNetworkHandler() == null) return;

        if (returnSlot != -1) {
            ticks++;
            if (ticks >= delay.getInt()) {
                InventoryUtils.pickHotbar(client, returnSlot);
                returnSlot = -1;
                ticks = 0;
            }
            return;
        }

        if (!(client.crosshairTarget instanceof EntityHitResult hit)) return;
        if (!(hit.getEntity() instanceof PlayerEntity target)) return;
        if (!target.isBlocking()) return;

        int axe = findAxe(client);
        if (axe == -1) return;
        int current = client.player.getInventory().selectedSlot;
        if (current == axe) return;

        returnSlot = current;
        ticks = 0;
        InventoryUtils.pickHotbar(client, axe);
    }

    private int findAxe(MinecraftClient client) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            if (stack.getItem() instanceof AxeItem) {
                return i;
            }
        }
        return -1;
    }
}
