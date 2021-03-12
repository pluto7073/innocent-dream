package io.innocent.dream.actions.direction;

import io.innocent.dream.actions.Action;
import io.innocent.dream.entities.Entities;

public class JumpAction extends Action {

    private boolean ready = true;

    public JumpAction() {
        super("key_jump");
    }

    @Override
    public void preformAction() {
        if (ready) {
            if (!Entities.PLAYER.isInventoryShown()) {
                Entities.PLAYER.jump();
                ready = false;
            }
        }
    }

    @Override
    public void afterActionPreformed() {
        ready = true;
    }
}
