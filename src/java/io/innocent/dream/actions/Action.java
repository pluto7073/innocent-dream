package io.innocent.dream.actions;

import io.innocent.dream.util.Settings;

public abstract class Action {

    private final String keyBindAddress;

    public Action(String keyBindAddress) {
        this.keyBindAddress = keyBindAddress;
    }

    public int getKey() {
        return Integer.parseInt((String) Settings.getSetting(keyBindAddress));
    }

    public abstract void preformAction();

    public void afterActionPreformed() {}

}
