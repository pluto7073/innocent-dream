package io.innocent.dream.entities;

import io.innocent.dream.drawing.Texture;

public abstract class EntityWithHP extends Entity {

    protected float hp;
    protected boolean dead = false;

    public EntityWithHP(Texture texture, float hp) {
        super(texture);
        this.hp = hp;
    }

    public void damage(float damageModifier) {
        this.hp -= damageModifier;
    }

    public abstract void die();

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    @Override
    public void tick() {
        if (!dead) {
            if (hp < 0.1f) {
                die();
                dead = true;
            }
        }
    }

}
