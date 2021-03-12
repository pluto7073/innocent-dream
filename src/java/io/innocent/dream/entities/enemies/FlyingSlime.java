package io.innocent.dream.entities.enemies;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.drawing.Texture;
import io.innocent.dream.entities.Enemy;
import io.innocent.dream.entities.Entities;
import io.innocent.dream.entities.EntityWithHP;
import io.innocent.dream.util.DisplayManager;
import io.innocent.dream.worldBuilder.WorldBuilder;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL11.*;

public class FlyingSlime extends Enemy {

    public final List<FlyingSlime> instances = new ArrayList<>();

    public FlyingSlime() {
        super(new Texture("assets/entities/enemies/flying_slime.png"), 10, 5, 2.5f);
        position = new Vector2f(0, 0);
    }

    @Override
    public void attack(EntityWithHP target) {
        int mod = ENTITY_RANDOM.nextInt(2) + 1;
        target.damage(attackDamage * (mod / 2f));
    }

    @Override
    public void render() {
        for (FlyingSlime slime : instances) {
            slime.renderInstance();
        }
    }

    private void renderInstance() {
        int width = 16;
        int height = 8;
        int texID = this.texture.getTextureID();
        glfwMakeContextCurrent(DisplayManager.win);
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
            glVertex2f(-width + position.x, -height + position.y);
        } glEnd();
        glDisable(GL_TEXTURE_2D);
    }

    @Override
    public void tick() {
        for (FlyingSlime s : instances) {
            s.tickInstance();
        }
    }

    public void tickInstance() {
        boolean canAttack;
        float disX = position.x - Entities.PLAYER.getPosition().x;
        float disY = position.y - Entities.PLAYER.getPosition().y;
        float subtotal = (disX * disX) + (disY * disY);
        float total = (float) Math.sqrt(subtotal);
        canAttack = total <= 32;
        if (coolDown < 1000) canAttack = false;
        int attackChance = ENTITY_RANDOM.nextInt(50);
        if (canAttack) {
            if (attackChance == 0) {
                this.attack(Entities.PLAYER);
                this.jump();
            }
        } else {
            if (attackChance == 5) {
                jump();
            }
        }
        position.x += horizontalVelocity * InnocentDream.timer.getTimeDifference();
        position.y += verticalVelocity * InnocentDream.timer.getTimeDifference();
        if (!moving) horizontalVelocity = 0f;
        if (testDown()) {
            verticalVelocity += WorldBuilder.GRAVITY * Math.min(InnocentDream.timer.getTimeDifference(), 1);
            verticalVelocity = Math.max(verticalVelocity, -2.5f);
        } else {
            verticalVelocity = 0f;
            inAir = false;
        }
        coolDown();
    }

    public void jump() {
        if (!inAir) {
            inAir = true;
            verticalVelocity = JUMP_SPEED;
            while (!testDown())
                position.y += 1f;
            float xDis = position.x - Entities.PLAYER.getPosition().x;
            if (xDis > 0) {
                if (testLeft()) {
                    horizontalVelocity = -SPEED * InnocentDream.timer.getTimeDifference();
                } else {
                    horizontalVelocity = 0;
                }
            } else {
               if (testRight()) {
                   horizontalVelocity = SPEED * InnocentDream.timer.getTimeDifference();
               } else {
                   horizontalVelocity = 0;
               }
            }
            moving = true;
        }
    }

    public void createInstance(float x, float y) {
        FlyingSlime slime = new FlyingSlime();
        slime.position.x = x;
        slime.position.y = y;
        instances.add(slime);
    }

    public void createInstance(float x, float y, float hp) {
        if (instances.size() < WorldBuilder.world.getEntityCap()) {
            FlyingSlime slime = new FlyingSlime();
            slime.position.x = x;
            slime.position.y = y;
            slime.setHp(hp);
            instances.add(slime);
        }
    }

    @Override
    public void die() {
        Entities.FLYING_SLIME.instances.remove(this);
        InnocentDream.apiManager.score(killScore, 594733);
    }

    public List<FlyingSlime> getInstances() {
        return instances;
    }
}
