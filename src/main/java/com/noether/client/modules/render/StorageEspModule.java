package com.noether.client.modules.render;

import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.render.Render3DUtils;
import com.noether.client.settings.BooleanSetting;
import com.noether.client.settings.ModeSetting;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;

public class StorageEspModule extends Module {
    private final ModeSetting mode = add(new ModeSetting("Mode", "Стиль", "Outline", "Outline", "Fill", "Both"));
    private final BooleanSetting chests = add(new BooleanSetting("Chests", "Сундуки", true));
    private final BooleanSetting ender = add(new BooleanSetting("Ender", "Эндер-сундуки", true));
    private final BooleanSetting shulkers = add(new BooleanSetting("Shulkers", "Шалкеры", true));
    private final BooleanSetting barrels = add(new BooleanSetting("Barrels", "Бочки", true));
    private final BooleanSetting hoppers = add(new BooleanSetting("Hoppers", "Воронки", false));

    public StorageEspModule() {
        super("StorageESP", "Подсветка сундуков, шалкеров, эндер-сундуков", Category.RENDER);
    }

    public void render(MatrixStack matrices) {
        if (!isEnabled()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;

        List<BlockEntity> snapshot = new ArrayList<>();
        int rd = client.options.getClampedViewDistance();
        BlockPos origin = client.player.getBlockPos();
        int cx = origin.getX() >> 4;
        int cz = origin.getZ() >> 4;
        try {
            for (int x = cx - rd; x <= cx + rd; x++) {
                for (int z = cz - rd; z <= cz + rd; z++) {
                    WorldChunk chunk = client.world.getChunk(x, z);
                    snapshot.addAll(chunk.getBlockEntities().values());
                }
            }
        } catch (Exception ignored) {
            return;
        }

        boolean fill = mode.is("Fill") || mode.is("Both");
        boolean outline = mode.is("Outline") || mode.is("Both");
        for (BlockEntity be : snapshot) {
            int color = colorOf(be);
            if (color == 0) continue;
            BlockPos p = be.getPos();
            Box box = new Box(p.getX(), p.getY(), p.getZ(), p.getX() + 1, p.getY() + 1, p.getZ() + 1);
            if (fill) Render3DUtils.box(matrices, box, color, true);
            if (outline) Render3DUtils.box(matrices, box, color, false);
        }
    }

    private int colorOf(BlockEntity be) {
        if (chests.getBool() && be instanceof ChestBlockEntity) return 0xFFFFC107;
        if (ender.getBool() && be instanceof EnderChestBlockEntity) return 0xFF7C3AED;
        if (shulkers.getBool() && be instanceof ShulkerBoxBlockEntity) return 0xFFEC4899;
        if (barrels.getBool() && be instanceof BarrelBlockEntity) return 0xFF92400E;
        if (hoppers.getBool() && be instanceof HopperBlockEntity) return 0xFF9CA3AF;
        return 0;
    }
}
