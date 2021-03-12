package io.innocent.dream.entities;

import io.innocent.dream.drawing.Texture;
import org.joml.Random;
import org.joml.Vector2f;

public abstract class Entity {

    public static final Random ENTITY_RANDOM = new Random();

    protected Texture texture;
    protected Vector2f position;

    public Entity(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getTextureID() {
        return texture.getTextureID();
    }

    public Vector2f getPosition() {
        return position;
    }

    public abstract void render();
    public abstract void tick();

}
