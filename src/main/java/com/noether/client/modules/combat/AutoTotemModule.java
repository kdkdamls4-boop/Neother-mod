package com.noether.client.modules.combat;

import com.noether.client.core.InventoryUtils;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.BooleanSetting;
import com.noether.client.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;

public class AutoTotemModule extends Module {
    private final NumberSetting hp = add(new NumberSetting("HP Threshold", "Свап при HP ниже порога (0 = всегда)", 8, 0, 20, 0.5));
    private final BooleanSetting smart = add(new BooleanSetting("Smart", "Только при реальной угрозе", true));
    private final BooleanSetting restore = add(new BooleanSetting("Restore", "Вернуть предмет в оффхэнд", true));

    /** Inventory index where the previous offhand item was parked after the swap. */
    private int parkedOffhandItemSlot = -1;
    private ItemStack previousOffhand = ItemStack.EMPTY;
    private boolean holdingTotem;

    public AutoTotemModule() {
        super("AutoTotem", "Умный свап тотема в оффхэнд с порогом HP", Category.COMBAT);
    }

    @Override
    protected void onDisable() {
        parkedOffhandItemSlot = -1;
        previousOffhand = ItemStack.EMPTY;
        holdingTotem = false;
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.interactionManager == null) return;
        if (!(client.player.currentScreenHandler instanceof PlayerScreenHandler) && client.currentScreen != null) {
            return;
        }

        boolean offhandIsTotem = client.player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING);
        float health = client.player.getHealth() + client.player.getAbsorptionAmount();
        boolean danger = health <= hp.getFloat() || !smart.getBool() || hp.getFloat() <= 0.01f;

        if (offhandIsTotem) {
            holdingTotem = true;
            if (restore.getBool() && smart.getBool() && hp.getFloat() > 0 && health > hp.getFloat() + 4
                    && parkedOffhandItemSlot != -1) {
                InventoryUtils.swapToOffhand(client, parkedOffhandItemSlot);
                parkedOffhandItemSlot = -1;
                previousOffhand = ItemStack.EMPTY;
                holdingTotem = false;
            }
            return;
        }

        if (!danger) return;

        int totemSlot = InventoryUtils.findItem(client.player, Items.TOTEM_OF_UNDYING);
        if (totemSlot == -1 || totemSlot == 40) return;

        ItemStack offhand = client.player.getOffHandStack();
        if (!offhand.isEmpty() && previousOffhand.isEmpty()) {
            previousOffhand = offhand.copy();
            // SWAP puts the current offhand item into totemSlot.
            parkedOffhandItemSlot = totemSlot;
        }
        InventoryUtils.swapToOffhand(client, totemSlot);
        holdingTotem = true;
    }

    public boolean isHoldingTotem() {
        return holdingTotem;
    }
}
