package io.innocent.dream.item;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.drawing.Texture;
import io.innocent.dream.entities.Entities;
import io.innocent.dream.entities.Entity;
import io.innocent.dream.registry.Registry;
import io.innocent.dream.tile.Tile;
import io.innocent.dream.util.DisplayManager;
import io.innocent.dream.worldBuilder.WorldTileManager;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class ItemEntity extends Entity {
	
	private static final float FALL_SPEED = 1.0f;

    protected String itemName;
    protected int count;

    public ItemEntity(String itemName, int count, Vector2f pos) {
        super(new Texture("assets/items/" + itemName + ".png"), "0");
        this.itemName = itemName;
        this.count = count;
        this.position = pos;
    }

    @Override
    public void render() {
        int size = 4;
        glfwMakeContextCurrent(DisplayManager.win);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        glBegin(GL_QUADS); {
            glTexCoord2f(0, 0);
            glVertex2f(-size + position.x, size + position.y);
            glTexCoord2f(1, 0);
            glVertex2f(size + position.x, size + position.y);
            glTexCoord2f(1, 1);
            glVertex2f(size + position.x, -size + position.y);
            glTexCoord2f(0, 1);
            glVertex2f(-size + position.x, -size + position.y);
        } glEnd();
        glDisable(GL_TEXTURE_2D);
    }

    @Override
    public void tick() {
        float dx = position.x - Entities.PLAYER.getPosition().x;
        float dy = position.y - Entities.PLAYER.getPosition().y;
        dx *= dx;
        dy *= dy;
        float a = dx + dy;
        float b = (float) Math.sqrt(a);
        if (b <= 16) {
            Items.ITEMS_TO_REMOVE.add(this);
            Entities.PLAYER.inventory.addItem(Registry.getItem(itemName));
        }
        if (testDown()) {
        	position.y -= FALL_SPEED * InnocentDream.timer.getTimeDifference();
        } else {
        	position.y += 0.1f;
        }
    }
    
    private boolean testDown() {
        int x = (int) (position.x / 16f);
        float y_ = (position.y / 16f) - 2f;
        int y = (int) Math.ceil(y_);
        x = WorldTileManager.verifyX(x);
        y = WorldTileManager.verifyY(y);
        Tile.Type tileType = WorldTileManager.getTileAtPosition(x, y).getType();
        return !tileType.equals(Tile.Type.BLOCK);
    }

}
