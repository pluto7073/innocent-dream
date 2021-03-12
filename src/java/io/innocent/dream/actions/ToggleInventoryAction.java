package io.innocent.dream.actions;

import io.innocent.dream.entities.Entities;

public class ToggleInventoryAction extends Action {

    private boolean ready = false;

    public ToggleInventoryAction() {
        super("key_inventory");
    }

    @Override
    public void preformAction() {
        ready = true;
    }

    @Override
    public void afterActionPreformed() {
        if (ready) {
            ready = false;
            Entities.PLAYER.toggleInventory();
        }
    }
}
