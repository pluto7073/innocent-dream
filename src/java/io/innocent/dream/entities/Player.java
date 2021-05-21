package io.innocent.dream.entities;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.drawing.Texture;
import io.innocent.dream.drawing.TextureRenderer;
import io.innocent.dream.inventory.PlayerInventory;
import io.innocent.dream.tile.Tile;
import io.innocent.dream.util.DisplayManager;
import io.innocent.dream.worldBuilder.WorldBuilder;
import io.innocent.dream.worldBuilder.WorldTileManager;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import java.io.File;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL11.*;

public class Player extends EntityWithHP {

    public static final float SPEED = 0.5f;
    public static final float JUMP_SPEED = 7.0f;
    public static int VIGNETTE = TextureRenderer.loadTexture("assets/vignette.png");

    private float horizontalVelocity = 0f;
    private float verticalVelocity = 0f;

    private final Texture side;
    private boolean inAir = true;
    private boolean inventoryShown = false;

    public boolean left = true;
    public boolean moving = false;

    public PlayerInventory inventory;

    public Player() {
        super(new Texture("assets/entities/character.png"), 20f, "9deeae54-b5c3-11eb-8529-0242ac130003");
        position = new Vector2f(0, 128);
        side = new Texture("assets/entities/character_side.png");
        inventory = new PlayerInventory();
    }

    public boolean isInventoryShown() {
        return inventoryShown;
    }

    public void toggleInventory() {
        inventoryShown = !inventoryShown;
    }

    @Override
    public void render() {
        int texID = texture.getTextureID();
        int width = 8;
        int height = 16;
        if (moving) {
            texID = side.getTextureID();
            if (left) {
                width *= -1;
            }
        }

        //Player
        glfwMakeContextCurrent(DisplayManager.win);
        glMatrixMode(GL_TEXTURE);
        glEnable(GL_TEXTURE_2D);
        GL11.glEnable(GL_ALPHA);
        GL11.glEnable(GL_BLEND);
        GL11.glEnable(GL_DEPTH);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPushMatrix();
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
        //Haze
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, VIGNETTE);
        glBegin(GL_QUADS); {
            int x = (int) Math.max(position.x, -InnocentDream.maxScroll);
            x = (int) Math.min(x, InnocentDream.maxScroll);
            glTexCoord2f(0, 0);
            glVertex2f(-260 + position.x, 130 + position.y);

            glTexCoord2f(1, 0);
            glVertex2f(260 + position.x, 130 + position.y);

            glTexCoord2f(1, 1);
            glVertex2f(260 + position.x, -130 + position.y);

            glTexCoord2f(0, 1);
            glVertex2f(-260 + position.x, -130 + position.y);
        } glEnd();
        glDisable(GL_TEXTURE_2D);

        glColor4f(0, 0, 0, 1);
        //Backup Haze
        glBegin(GL_QUADS); {
            glVertex2f(-512 + position.x, 130 + position.y);
            glVertex2f(-260 + position.x, 130 + position.y);
            glVertex2f(-260 + position.x, -130 + position.y);
            glVertex2f(-512 + position.x, -130 + position.y);
        } glEnd();
        glBegin(GL_QUADS); {
            glVertex2f(512 + position.x, 130 + position.y);
            glVertex2f(260 + position.x, 130 + position.y);
            glVertex2f(260 + position.x, -130 + position.y);
            glVertex2f(512 + position.x, -130 + position.y);
        } glEnd();
        glColor4f(1, 1, 1, 1);

        if (inventoryShown) {
            inventory.renderInventory();
            moving = false;
            left = false;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!moving) horizontalVelocity = 0f;
        if (testDown()) {
            verticalVelocity += WorldBuilder.GRAVITY * Math.min(InnocentDream.timer.getTimeDifference(), 1);
            verticalVelocity = Math.max(verticalVelocity, -2.5f);
        } else {
            verticalVelocity = 0f;
            inAir = false;
        }
        if (!testUp()) verticalVelocity = -1;
        if (position.y / 16 < -32) die();
        position.x += horizontalVelocity * InnocentDream.timer.getTimeDifference();
        position.y += verticalVelocity * InnocentDream.timer.getTimeDifference();
        glfwMakeContextCurrent(DisplayManager.win);
        glMatrixMode(GL11.GL_PROJECTION);
        glLoadIdentity();
        Vector2f position = this.position;
        float min = WorldTileManager.MIN_CAM_X;
        float max = WorldTileManager.MAX_CAM_X;
        int x = (int) Math.max(position.x, min);
        x = (int) Math.min(x, max);
        glOrtho(-InnocentDream.width + x,
                InnocentDream.width + x,
                -128 + position.y,
                128 + position.y,
                -1, 1);
        glMatrixMode(GL11.GL_MODELVIEW);
    }

    private boolean testLeft() {
        int x = (int) ((position.x / 16) - 0.5f);
        int y;
        if (position.y < 0) y = (int) (position.y / 16) - 1;
        else y = (int) (position.y / 16);
        if (x < (WorldTileManager.MIN_X) + 0.5f) return false;
        x = WorldTileManager.verifyX(x);
        y = WorldTileManager.verifyY(y);
        Tile.Type tileType = WorldTileManager.getTileAtPosition(x, y).getType();
        return !(tileType.equals(Tile.Type.BLOCK));
    }

    private boolean testRight() {
        int x = (int) ((position.x / 16) + 0.5f);
        int y;
        if (position.y < 0) y = (int) (position.y / 16) - 1;
        else y = (int) (position.y / 16);
        if (x > (WorldTileManager.MAX_X) - 0.5f) return false;
        x = WorldTileManager.verifyX(x);
        y = WorldTileManager.verifyY(y);
        Tile.Type tileType = WorldTileManager.getTileAtPosition(x, y).getType();
        return !(tileType.equals(Tile.Type.BLOCK));
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

    private boolean testUp() {
        int x = (int) (position.x / 16f);
        float y_ = (position.y / 16f) + 1f;
        int y = (int) Math.floor(y_);
        x = WorldTileManager.verifyX(x);
        y = WorldTileManager.verifyY(y);
        Tile.Type tileType = WorldTileManager.getTileAtPosition(x, y).getType();
        return !tileType.equals(Tile.Type.BLOCK);
    }

    public void left() {
        if (testLeft()) {
            horizontalVelocity = -SPEED;
        } else {
            horizontalVelocity = 0f;
        }
    }

    public void right() {
        if (testRight()) {
            horizontalVelocity = SPEED;
        } else {
            horizontalVelocity = 0f;
        }
    }

    public void jump() {
        if (!inAir) {
            inAir = true;
            verticalVelocity = JUMP_SPEED / InnocentDream.timer.getTimeDifference();
            while (!testDown())
                position.y += 1;
        }
    }

    @Override
    public void die() {
        System.out.println("YOU DIED");
        InnocentDream.CURRENT_SCREEN = InnocentDream.MAIN_MENU;
        InnocentDream.MAIN_GAME = null;
        File file = new File(InnocentDream.path + "\\worlds\\world.id");
        file.delete();
        position.x = 0;
        position.y = 256;
        hp = 20f;
        dead = false;
        Entities.FLYING_SLIME.instances.clear();
    }

}
