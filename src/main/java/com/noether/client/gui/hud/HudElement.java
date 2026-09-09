package com.noether.client.gui.hud;

import net.minecraft.client.gui.DrawContext;

public abstract class HudElement {
    private final String id;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean dragging;
    private int dragOffsetX;
    private int dragOffsetY;

    protected HudElement(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean isDragging() {
        return dragging;
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void startDrag(double mouseX, double mouseY) {
        dragging = true;
        dragOffsetX = (int) mouseX - x;
        dragOffsetY = (int) mouseY - y;
    }

    public void dragTo(double mouseX, double mouseY) {
        if (!dragging) return;
        x = (int) mouseX - dragOffsetX;
        y = (int) mouseY - dragOffsetY;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
    }

    public void stopDrag() {
        dragging = false;
    }

    public abstract void render(DrawContext context, float tickDelta);

    public abstract boolean isVisible();
}
