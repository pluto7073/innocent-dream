package io.innocent.dream.actions.direction;

import io.innocent.dream.actions.Action;
import io.innocent.dream.entities.Entities;

public class RightAction extends Action {

    public RightAction() {
        super("key_right");
    }

    @Override
    public void preformAction() {
        Entities.PLAYER.moving = true;
        Entities.PLAYER.left = false;
        if (!Entities.PLAYER.isInventoryShown())
            Entities.PLAYER.right();
    }

    @Override
    public void afterActionPreformed() {
        if (!Entities.PLAYER.left) {
            Entities.PLAYER.moving = false;
        }
    }

}
