package com.noether.client.modules.misc;

import com.noether.client.friend.FriendManager;
import com.noether.client.gui.hud.NotificationManager;
import com.noether.client.modules.Category;
import com.noether.client.modules.Module;
import net.minecraft.entity.player.PlayerEntity;

public class MiddleClickFriendModule extends Module {
    public MiddleClickFriendModule() {
        super("MiddleClickFriend", "Колёсико мыши — добавить / удалить друга", Category.MISC);
        setEnabled(true);
    }

    public void onMiddleClick(PlayerEntity player) {
        if (!isEnabled() || player == null) return;
        String name = player.getGameProfile().getName();
        boolean added = FriendManager.getInstance().toggle(name);
        NotificationManager.getInstance().friend(name, added);
    }
}
