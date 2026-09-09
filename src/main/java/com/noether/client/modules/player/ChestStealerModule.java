package com.noether.client.modules.player;

import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.BooleanSetting;
import com.noether.client.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ChestStealerModule extends Module {
    private final NumberSetting minDelay = add(new NumberSetting("Min Delay", "Мин. задержка мс", 20, 0, 200, 5));
    private final NumberSetting maxDelay = add(new NumberSetting("Max Delay", "Макс. задержка мс", 50, 0, 250, 5));
    private final BooleanSetting smart = add(new BooleanSetting("Smart Filter", "Только ценные предметы", true));
    private final BooleanSetting autoClose = add(new BooleanSetting("AutoClose", "Закрыть после лута", true));

    private static final Set<Item> VALUABLE = Set.of(
            Items.TOTEM_OF_UNDYING, Items.ENDER_PEARL, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE,
            Items.END_CRYSTAL, Items.OBSIDIAN, Items.RESPAWN_ANCHOR, Items.GLOWSTONE,
            Items.NETHERITE_INGOT, Items.NETHERITE_SWORD, Items.NETHERITE_AXE, Items.NETHERITE_HELMET,
            Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS,
            Items.DIAMOND, Items.DIAMOND_SWORD, Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE,
            Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS, Items.EXPERIENCE_BOTTLE, Items.GOLDEN_CARROT,
            Items.SHULKER_BOX, Items.ENCHANTED_BOOK, Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION,
            Items.ELYTRA, Items.TRIDENT, Items.ARROW, Items.SPECTRAL_ARROW, Items.TIPPED_ARROW
    );

    private long nextClick;

    public ChestStealerModule() {
        super("ChestStealer", "Автолутание сундуков с рандомизацией задержек", Category.PLAYER);
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.interactionManager == null) return;
        if (!(client.currentScreen instanceof GenericContainerScreen)) return;

        long now = System.currentTimeMillis();
        if (now < nextClick) return;

        ScreenHandler handler = client.player.currentScreenHandler;
        int chestSlots = handler.slots.size() - 36;
        boolean stole = false;
        for (int i = 0; i < chestSlots; i++) {
            Slot slot = handler.slots.get(i);
            ItemStack stack = slot.getStack();
            if (stack.isEmpty()) continue;
            if (smart.getBool() && !isValuable(stack)) continue;
            client.interactionManager.clickSlot(handler.syncId, i, 0, SlotActionType.QUICK_MOVE, client.player);
            stole = true;
            schedule();
            break;
        }

        if (!stole && autoClose.getBool()) {
            client.player.closeHandledScreen();
        }
    }

    private void schedule() {
        int min = minDelay.getInt();
        int max = Math.max(min, maxDelay.getInt());
        nextClick = System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private boolean isValuable(ItemStack stack) {
        if (stack.hasEnchantments()) return true;
        return VALUABLE.contains(stack.getItem()) || stack.getName().getString().toLowerCase().contains("shulker");
    }
}
