package com.noether.client.modules.movement;

import com.noether.client.config.ThemeManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.render.Render3DUtils;
import com.noether.client.settings.ColorSetting;
import com.noether.client.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JumpCirclesModule extends Module {
    private final NumberSetting duration = add(new NumberSetting("Duration", "Время жизни сек", 1.4, 0.4, 3.0, 0.1));
    private final ColorSetting color = add(new ColorSetting("Color", "Цвет круга", 0xFFA78BFA));

    private final List<Circle> circles = new ArrayList<>();
    private boolean wasOnGround = true;

    public JumpCirclesModule() {
        super("JumpCircles", "Расширяющиеся круги на земле при прыжке", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        boolean ground = client.player.isOnGround();
        if (wasOnGround && !ground && client.player.getVelocity().y > 0) {
            Vec3d pos = client.player.getPos();
            circles.add(new Circle(pos.x, pos.y, pos.z, System.currentTimeMillis()));
        }
        wasOnGround = ground;
    }

    public void render(MatrixStack matrices) {
        if (!isEnabled()) return;
        long now = System.currentTimeMillis();
        long life = (long) (duration.get() * 1000);
        Iterator<Circle> it = circles.iterator();
        while (it.hasNext()) {
            Circle c = it.next();
            float t = (now - c.time) / (float) life;
            if (t >= 1) {
                it.remove();
                continue;
            }
            float radius = 0.3f + t * 1.6f;
            int col = ThemeManager.getTheme() == ThemeManager.Theme.RAINBOW
                    ? com.noether.client.util.ColorUtils.rainbow(t)
                    : color.getRgb();
            int alpha = (int) ((1.0f - t) * 200);
            Render3DUtils.circle(matrices, c.x, c.y + 0.02, c.z, radius, com.noether.client.util.ColorUtils.withAlpha(col, alpha));
        }
    }

    private record Circle(double x, double y, double z, long time) {}
}
