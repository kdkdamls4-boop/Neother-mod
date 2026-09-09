package com.noether.client.friend;

import java.util.List;
import java.util.Locale;

/**
 * Parses `/friend` chat commands. Case-insensitive. Pure string API so it is unit-testable.
 */
public final class FriendCommands {
    private FriendCommands() {}

    public static boolean isFriendCommand(String message) {
        if (message == null) return false;
        String trimmed = message.trim();
        return trimmed.toLowerCase(Locale.ROOT).startsWith("/friend");
    }

    public static String handle(String message) {
        return handle(message, FriendManager.getInstance());
    }

    public static String handle(String message, FriendManager manager) {
        if (message == null) {
            return "§cПустая команда.";
        }
        String[] parts = message.trim().split("\\s+");
        if (parts.length == 0 || !parts[0].equalsIgnoreCase("/friend")) {
            return "§cНе команда /friend.";
        }
        if (parts.length == 1) {
            return usage();
        }
        String sub = parts[1].toLowerCase(Locale.ROOT);
        return switch (sub) {
            case "add" -> {
                if (parts.length < 3) yield "§cИспользование: /friend add <ник>";
                String name = parts[2];
                yield manager.add(name)
                        ? "§aДобавлен в друзья: §f" + name
                        : "§eУже в списке друзей: §f" + name;
            }
            case "remove", "del", "delete" -> {
                if (parts.length < 3) yield "§cИспользование: /friend remove <ник>";
                String name = parts[2];
                yield manager.remove(name)
                        ? "§cУдалён из друзей: §f" + name
                        : "§eНе найден в друзьях: §f" + name;
            }
            case "list" -> {
                List<String> list = manager.list();
                if (list.isEmpty()) yield "§7Список друзей пуст.";
                yield "§dДрузья §7(" + list.size() + ")§f: " + String.join("§7, §f", list);
            }
            case "clear" -> {
                manager.clear();
                yield "§cСписок друзей очищен.";
            }
            default -> usage();
        };
    }

    private static String usage() {
        return "§d/friend §fadd <ник> §8| §fremove <ник> §8| §flist §8| §fclear";
    }
}
