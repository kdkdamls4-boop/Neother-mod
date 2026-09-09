package com.noether.client.modules.combat;

import com.noether.client.friend.FriendManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import com.noether.client.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class HitBoxesModule extends Module {
    private final NumberSetting expand = add(new NumberSetting("Expand", "Расширение хитбокса", 0.3, 0.0, 1.5, 0.05));

    public HitBoxesModule() {
        super("HitBoxes", "Расширение хитбоксов врагов", Category.COMBAT);
    }

    public float expandFor(Entity entity) {
        if (!isEnabled() || entity == null) return 0f;
        if (!(entity instanceof PlayerEntity player)) return 0f;
        if (player.isMainPlayer()) return 0f;
        if (FriendManager.getInstance().isFriend(player.getGameProfile().getName())) return 0f;
        return expand.getFloat();
    }
}
