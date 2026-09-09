package com.noether.client.core;

import com.noether.client.friend.FriendManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public final class TargetManager {
    public enum Priority {
        DISTANCE, HEALTH, ANGLE
    }

    private static LivingEntity current;

    private TargetManager() {}

    public static LivingEntity getCurrent() {
        return current;
    }

    public static void setCurrent(LivingEntity entity) {
        current = entity;
    }

    public static void clear() {
        current = null;
    }

    public static LivingEntity find(MinecraftClient client, double range, Priority priority, boolean playersOnly, boolean wallCheck) {
        if (client.player == null || client.world == null) {
            return null;
        }
        Vec3d eye = client.player.getEyePos();
        List<? extends LivingEntity> candidates;
        if (playersOnly) {
            candidates = client.world.getPlayers().stream()
                    .filter(p -> isValid(client, p, range))
                    .toList();
        } else {
            candidates = client.world.getEntitiesByClass(LivingEntity.class,
                    client.player.getBoundingBox().expand(range + 1.0),
                    e -> isValid(client, e, range));
        }

        Comparator<LivingEntity> cmp = switch (priority) {
            case HEALTH -> Comparator.comparingDouble(LivingEntity::getHealth);
            case ANGLE -> Comparator.comparingDouble(e -> angleTo(client, e));
            default -> Comparator.comparingDouble(e -> e.squaredDistanceTo(client.player));
        };

        LivingEntity best = candidates.stream()
                .filter(e -> !wallCheck || canSee(client, eye, e))
                .min(cmp)
                .orElse(null);
        current = best;
        return best;
    }

    public static boolean isValid(MinecraftClient client, LivingEntity entity, double range) {
        if (entity == null || entity == client.player || !entity.isAlive() || entity.isRemoved()) {
            return false;
        }
        if (entity instanceof PlayerEntity player) {
            if (player.isSpectator() || player.isCreative()) {
                return false;
            }
            String name = player.getGameProfile().getName();
            if (FriendManager.getInstance().isFriend(name)) {
                return false;
            }
        }
        return entity.squaredDistanceTo(client.player) <= range * range;
    }

    public static boolean canSee(MinecraftClient client, Vec3d eye, Entity entity) {
        if (client.world == null) return false;
        Box box = entity.getBoundingBox();
        Vec3d target = new Vec3d(MathHelper.clamp(eye.x, box.minX, box.maxX),
                MathHelper.clamp(eye.y, box.minY, box.maxY),
                MathHelper.clamp(eye.z, box.minZ, box.maxZ));
        HitResult result = client.world.raycast(new RaycastContext(
                eye, target,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                client.player
        ));
        return result.getType() == HitResult.Type.MISS || result.getPos().squaredDistanceTo(target) < 0.36;
    }

    public static double angleTo(MinecraftClient client, Entity entity) {
        Vec3d diff = entity.getEyePos().subtract(client.player.getEyePos()).normalize();
        Vec3d look = client.player.getRotationVec(1.0f).normalize();
        return Math.toDegrees(Math.acos(MathHelper.clamp(look.dotProduct(diff), -1, 1)));
    }

    public static boolean isFriendName(String name) {
        return name != null && FriendManager.getInstance().isFriend(name.toLowerCase(Locale.ROOT));
    }

    public static AbstractClientPlayerEntity asClientPlayer(LivingEntity entity) {
        return entity instanceof AbstractClientPlayerEntity abs ? abs : null;
    }
}
