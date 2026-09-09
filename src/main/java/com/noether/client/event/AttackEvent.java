package com.noether.client.event;

public class AttackEvent extends CancellableEvent {
    private final String targetName;

    public AttackEvent(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetName() {
        return targetName;
    }
}
