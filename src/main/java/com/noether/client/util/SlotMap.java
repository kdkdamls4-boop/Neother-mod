package com.noether.client.util;

/**
 * PlayerInventory index → PlayerScreenHandler slot.
 * Hotbar 0–8 → 36–44, main 9–35 → 9–35,
 * armor 36–39 (boots→helm) → 8–5, offhand 40 → 45.
 */
public final class SlotMap {
    private SlotMap() {}

    public static int inventoryToHandler(int invSlot) {
        if (invSlot >= 0 && invSlot < 9) {
            return 36 + invSlot;
        }
        if (invSlot >= 9 && invSlot < 36) {
            return invSlot;
        }
        if (invSlot >= 36 && invSlot < 40) {
            return 8 - (invSlot - 36);
        }
        if (invSlot == 40) {
            return 45;
        }
        return -1;
    }
}
