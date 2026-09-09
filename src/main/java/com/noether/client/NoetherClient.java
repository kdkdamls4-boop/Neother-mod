package com.noether.client;

import com.noether.client.config.ConfigManager;
import com.noether.client.core.EventBus;
import com.noether.client.core.ModuleManager;
import com.noether.client.friend.FriendCommands;
import com.noether.client.gui.hud.HudManager;
import com.noether.client.gui.hud.Notification;
import com.noether.client.gui.hud.NotificationManager;
import com.noether.client.modules.combat.HitParticlesModule;
import com.noether.client.modules.movement.JumpCirclesModule;
import com.noether.client.modules.movement.TrailsModule;
import com.noether.client.modules.render.BoxEspModule;
import com.noether.client.modules.render.ChinaHatModule;
import com.noether.client.modules.render.NameTagsModule;
import com.noether.client.modules.render.StorageEspModule;
import com.noether.client.modules.render.TargetEspModule;
import com.noether.client.modules.render.TracersModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class NoetherClient implements ClientModInitializer {
    public static final String MOD_ID = "noether";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LoggerFactory.getLogger("Noether");

    private static NoetherClient instance;
    private ConfigManager config;
    private final EventBus events = new EventBus();

    public static NoetherClient getInstance() {
        return instance;
    }

    public ConfigManager getConfig() {
        return config;
    }

    public EventBus getEvents() {
        return events;
    }

    @Override
    public void onInitializeClient() {
        instance = this;
        LOGGER.info("Noether Client {} booting", VERSION);

        ModuleManager.getInstance().init();
        HudManager.getInstance().init();

        Path dir = MinecraftClient.getInstance().runDirectory.toPath().resolve("noether");
        config = new ConfigManager(dir.resolve("config.json"));
        try {
            config.loadAndApply();
        } catch (Exception e) {
            LOGGER.warn("Failed to load config: {}", e.getMessage());
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> ModuleManager.getInstance().onTick());

        HudRenderCallback.EVENT.register((context, tickDelta) -> HudManager.getInstance().render(context, tickDelta));

        WorldRenderEvents.LAST.register(ctx -> {
            var matrices = ctx.matrixStack();
            ModuleManager mm = ModuleManager.getInstance();
            BoxEspModule box = mm.get(BoxEspModule.class);
            if (box != null) box.render(matrices);
            TracersModule tracers = mm.get(TracersModule.class);
            if (tracers != null) tracers.render(matrices);
            StorageEspModule storage = mm.get(StorageEspModule.class);
            if (storage != null) storage.render(matrices);
            TargetEspModule targetEsp = mm.get(TargetEspModule.class);
            if (targetEsp != null) targetEsp.render(matrices);
            ChinaHatModule hat = mm.get(ChinaHatModule.class);
            if (hat != null) hat.render(matrices);
            JumpCirclesModule circles = mm.get(JumpCirclesModule.class);
            if (circles != null) circles.render(matrices);
            TrailsModule trails = mm.get(TrailsModule.class);
            if (trails != null) trails.render(matrices);
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            ModuleManager mm = ModuleManager.getInstance();
            NameTagsModule tags = mm.get(NameTagsModule.class);
            if (tags != null) tags.render2D(context);
            TargetEspModule targetEsp = mm.get(TargetEspModule.class);
            if (targetEsp != null) targetEsp.render2D(context);
        });

        ClientSendMessageEvents.ALLOW_CHAT.register(message -> {
            if (FriendCommands.isFriendCommand(message)) {
                String response = FriendCommands.handle(message);
                NotificationManager.getInstance().push("Friends", response.replaceAll("§.", ""), Notification.Type.INFO);
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player != null) {
                    client.player.sendMessage(net.minecraft.text.Text.literal(response), false);
                }
                try {
                    config.save();
                } catch (Exception ignored) {
                }
                return false;
            }
            return true;
        });

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hit) -> {
            if (!world.isClient) return ActionResult.PASS;
            if (entity instanceof net.minecraft.entity.player.PlayerEntity target
                    && com.noether.client.friend.FriendManager.getInstance()
                    .isFriend(target.getGameProfile().getName())) {
                return ActionResult.FAIL;
            }
            HitParticlesModule particles = ModuleManager.getInstance().get(HitParticlesModule.class);
            if (particles != null && particles.isEnabled()) {
                particles.onHit(entity, player.fallDistance > 0);
            }
            return ActionResult.PASS;
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (config != null) config.save();
            } catch (Exception ignored) {
            }
        }, "noether-save"));

        LOGGER.info("Noether Client ready · {} modules", ModuleManager.getInstance().getModules().size());
    }
}
