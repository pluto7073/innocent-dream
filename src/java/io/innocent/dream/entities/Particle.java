package io.innocent.dream.entities;

import io.innocent.dream.drawing.Texture;
import io.innocent.dream.util.DisplayManager;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class Particle extends Entity {

    private final Vector2f position;
    private final Texture texture;
    private final Vector2f size;
    private final int lifespan;
    private int i = 0;

    public Particle(Vector2f position, Texture texture, Vector2f size, int lifespan) {
        super(texture, "0");
        this.position = position;
        this.texture = texture;
        this.size = size;
        this.lifespan = lifespan;
    }

    @Override
    public void render() {
        glfwMakeContextCurrent(DisplayManager.win);
        int texID = texture.getTextureID();
        float width = this.size.x / 2f;
        float height = this.size.y / 2f;
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texID);
        glBegin(GL_QUADS); {
            glTexCoord2f(0, 0);
            glVertex2f(-width + position.x, height + position.y);
            glTexCoord2f(1, 0);
            glVertex2f(width + position.x, height + position.y);
            glTexCoord2f(1, 1);
            glVertex2f(width + position.x, -height + position.y);
            glTexCoord2f(0, 1);
            glTexCoord2f(-width + position.x, -height + position.y);
        } glEnd();
        glDisable(GL_TEXTURE_2D);
    }

    @Override
    public void tick() {
        i++;
        if (i == lifespan) {
            Particles.PARTICLES.remove(this);
        }
    }

}
