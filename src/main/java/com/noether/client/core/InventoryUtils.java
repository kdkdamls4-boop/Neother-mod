package com.noether.client.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

public final class InventoryUtils {
    private InventoryUtils() {}

    public static int findItem(ClientPlayerEntity player, Item item) {
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (!stack.isEmpty() && stack.isOf(item)) {
                return i;
            }
        }
        return -1;
    }

    public static int findHotbarItem(ClientPlayerEntity player, Item item) {
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.getStack(i);
            if (!stack.isEmpty() && stack.isOf(item)) {
                return i;
            }
        }
        return -1;
    }

    public static void swapToOffhand(MinecraftClient client, int invSlot) {
        if (client.interactionManager == null || client.player == null) return;
        int sync = client.player.currentScreenHandler.syncId;
        // Player inventory slot index in the survival screen handler: 9-44, offhand 45.
        int containerSlot = inventoryToHandler(invSlot);
        client.interactionManager.clickSlot(sync, containerSlot, 40, SlotActionType.SWAP, client.player);
    }

    public static void pickHotbar(MinecraftClient client, int hotbar) {
        if (client.player == null || client.getNetworkHandler() == null) return;
        client.player.getInventory().selectedSlot = hotbar;
        client.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(hotbar));
    }

    public static int inventoryToHandler(int invSlot) {
        if (invSlot >= 0 && invSlot < 9) {
            return 36 + invSlot;
        }
        return invSlot;
    }
}
