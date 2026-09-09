package com.noether.client.modules.render;

import com.noether.client.config.ThemeManager;
import com.noether.client.core.ModuleManager;
import com.noether.client.friend.FriendManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.render.Render3DUtils;
import com.noether.client.settings.BooleanSetting;
import com.noether.client.settings.ModeSetting;
import com.noether.client.util.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

public class BoxEspModule extends Module {
    private final ModeSetting mode = add(new ModeSetting("Mode", "2D / 3D", "3D", "3D", "2D"));
    private final BooleanSetting fill = add(new BooleanSetting("Fill", "Заливка", true));

    public BoxEspModule() {
        super("BoxESP", "2D/3D рамки вокруг игроков", Category.RENDER);
    }

    public void render(MatrixStack matrices) {
        if (!isEnabled()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;
        for (PlayerEntity player : client.world.getPlayers()) {
            if (player == client.player) continue;
            int color = FriendManager.getInstance().isFriend(player.getGameProfile().getName())
                    ? ColorUtils.friendGreen() : ThemeManager.accent();
            if (mode.is("3D")) {
                Render3DUtils.box(matrices, player.getBoundingBox(), color, fill.getBool());
            }
        }
    }

    public boolean is2D() {
        return isEnabled() && mode.is("2D");
    }

    public static BoxEspModule get() {
        return ModuleManager.getInstance().get(BoxEspModule.class);
    }
}
