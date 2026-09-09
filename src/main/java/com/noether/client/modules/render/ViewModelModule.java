package com.noether.client.modules.render;

import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.ModeSetting;
import com.noether.client.settings.NumberSetting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;

public class ViewModelModule extends Module {
    public final NumberSetting x = add(new NumberSetting("X", "Смещение X", 0.0, -2.0, 2.0, 0.05));
    public final NumberSetting y = add(new NumberSetting("Y", "Смещение Y", 0.0, -2.0, 2.0, 0.05));
    public final NumberSetting z = add(new NumberSetting("Z", "Смещение Z", 0.0, -2.0, 2.0, 0.05));
    public final ModeSetting anim = add(new ModeSetting("Swing", "Анимация удара", "Pulsar",
            "Pulsar", "1.7", "Swipe", "Spin", "Drop"));

    public ViewModelModule() {
        super("ViewModel", "Смещение рук + 5 анимаций удара", Category.RENDER);
    }

    public void apply(MatrixStack matrices, Arm arm, float swing) {
        if (!isEnabled()) return;
        float side = arm == Arm.RIGHT ? 1 : -1;
        matrices.translate(x.get() * side, y.get(), z.get());
        applySwing(matrices, side, swing);
    }

    private void applySwing(MatrixStack matrices, float side, float swing) {
        if (swing <= 0) return;
        float s = (float) Math.sin(swing * Math.PI);
        switch (anim.get()) {
            case "1.7" -> {
                matrices.translate(side * 0.56, -0.32, -0.72);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(side * 45));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-s * 80));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(side * 20));
            }
            case "Swipe" -> {
                matrices.translate(side * 0.1, -0.05, 0);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(side * s * 70));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(side * s * -35));
            }
            case "Spin" -> matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(swing * 360 * side));
            case "Drop" -> {
                matrices.translate(0, -s * 0.55, 0);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(s * 90));
            }
            default -> { // Pulsar
                matrices.translate(side * s * 0.18, s * 0.12, -s * 0.1);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-s * 25));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(side * s * 18));
            }
        }
    }
}
