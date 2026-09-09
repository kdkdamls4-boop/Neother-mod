package com.noether.client.modules;

public enum Category {
    COMBAT("Combat", "Боевые"),
    MOVEMENT("Movement", "Движение"),
    RENDER("Render", "Визуалы"),
    PLAYER("Player", "Игрок"),
    MISC("Misc", "Разное");

    private final String display;
    private final String displayRu;

    Category(String display, String displayRu) {
        this.display = display;
        this.displayRu = displayRu;
    }

    public String getDisplay() {
        return display;
    }

    public String getDisplayRu() {
        return displayRu;
    }
}
