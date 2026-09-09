package com.noether.client.friend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Persistent in-memory friend list. Names are stored with original casing
 * but compared case-insensitively.
 */
public final class FriendManager {
    private static final FriendManager INSTANCE = new FriendManager();
    private final Set<String> friends = new LinkedHashSet<>();

    private FriendManager() {}

    public static FriendManager getInstance() {
        return INSTANCE;
    }

    public boolean add(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        String trimmed = name.trim();
        if (isFriend(trimmed)) {
            return false;
        }
        friends.add(trimmed);
        return true;
    }

    public boolean remove(String name) {
        if (name == null) {
            return false;
        }
        String target = findStored(name);
        if (target == null) {
            return false;
        }
        friends.remove(target);
        return true;
    }

    public boolean toggle(String name) {
        if (isFriend(name)) {
            remove(name);
            return false;
        }
        add(name);
        return true;
    }

    public boolean isFriend(String name) {
        return findStored(name) != null;
    }

    public void clear() {
        friends.clear();
    }

    public List<String> list() {
        return Collections.unmodifiableList(new ArrayList<>(friends));
    }

    public Set<String> snapshot() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(friends));
    }

    public void replaceAll(Iterable<String> names) {
        friends.clear();
        if (names == null) return;
        for (String name : names) {
            add(name);
        }
    }

    private String findStored(String name) {
        if (name == null) return null;
        String needle = name.trim().toLowerCase(Locale.ROOT);
        if (needle.isEmpty()) return null;
        for (String stored : friends) {
            if (stored.toLowerCase(Locale.ROOT).equals(needle)) {
                return stored;
            }
        }
        return null;
    }
}
