package io.innocent.dream.actions.direction;

import io.innocent.dream.actions.Action;
import io.innocent.dream.entities.Entities;

public class LeftAction extends Action {

    public LeftAction() {
        super("key_left");
    }

    @Override
    public void preformAction() {
        Entities.PLAYER.moving = true;
        Entities.PLAYER.left = true;
        if (!Entities.PLAYER.isInventoryShown()) {
            Entities.PLAYER.left();
        }
    }

    @Override
    public void afterActionPreformed() {
        Entities.PLAYER.moving = false;
    }

}
