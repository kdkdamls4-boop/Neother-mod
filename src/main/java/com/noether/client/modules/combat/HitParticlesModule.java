package com.noether.client.modules.combat;

import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.BooleanSetting;
import com.noether.client.settings.ColorSetting;
import com.noether.client.settings.ModeSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class HitParticlesModule extends Module {
    private final ModeSetting mode = add(new ModeSetting("Mode", "Тип частиц", "Crit", "Crit", "Enchant", "Dust", "Soul"));
    private final BooleanSetting always = add(new BooleanSetting("Always", "Даже без крита", true));
    private final ColorSetting color = add(new ColorSetting("Dust Color", "Цвет Dust", 0xFFA78BFA));

    public HitParticlesModule() {
        super("HitParticles", "Кастомные частицы при ударах и критах", Category.COMBAT);
    }

    public void onHit(Entity target, boolean crit) {
        if (!isEnabled() || target == null) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;
        if (!always.getBool() && !crit) return;

        Vec3d pos = target.getPos().add(0, target.getHeight() * 0.6, 0);
        switch (mode.get()) {
            case "Enchant" -> spawn(client, pos, ParticleTypes.ENCHANTED_HIT, 12);
            case "Soul" -> spawn(client, pos, ParticleTypes.SOUL_FIRE_FLAME, 10);
            case "Dust" -> {
                int c = color.getRgb();
                Vector3f rgb = new Vector3f(((c >> 16) & 255) / 255f, ((c >> 8) & 255) / 255f, (c & 255) / 255f);
                for (int i = 0; i < 14; i++) {
                    client.world.addParticle(new DustParticleEffect(rgb, 1.1f),
                            pos.x + rand(), pos.y + rand(), pos.z + rand(), 0, 0.02, 0);
                }
            }
            default -> spawn(client, pos, ParticleTypes.CRIT, 16);
        }
    }

    private void spawn(MinecraftClient client, Vec3d pos, net.minecraft.particle.ParticleEffect type, int n) {
        for (int i = 0; i < n; i++) {
            client.world.addParticle(type, pos.x + rand(), pos.y + rand(), pos.z + rand(), 0, 0.05, 0);
        }
    }

    private double rand() {
        return (Math.random() - 0.5) * 0.6;
    }
}
