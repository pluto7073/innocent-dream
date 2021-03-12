package io.innocent.dream.entities;

import io.innocent.dream.drawing.Texture;
import io.innocent.dream.tile.Tile;
import io.innocent.dream.worldBuilder.WorldTileManager;

public abstract class Enemy extends EntityWithHP {

    protected final int killScore;
    protected final float attackDamage;
    protected float verticalVelocity = 0f;
    protected float horizontalVelocity = 0f;
    protected boolean moving = false;
    protected boolean inAir = true;
    protected int coolDown = 0;
    public static final float SPEED = 0.5f;
    public static final float JUMP_SPEED = 2.5f;

    public Enemy(Texture texture, int killScore, int hp, float attackDamage) {
        super(texture, hp);
        this.killScore = killScore;
        this.attackDamage = attackDamage;
    }

    public int getKillScore() {
        return killScore;
    }

    public abstract void attack(EntityWithHP target);

    protected boolean testLeft() {
        int x = (int) ((position.x / 16) - 0.5f);
        int y;
        if (position.y < 0) y = (int) (position.y / 16) - 1;
        else y = (int) (position.y / 16);
        if (x < (-1024 / 16f) + 0.5f) return false;
        x = WorldTileManager.verifyX(x);
        y = WorldTileManager.verifyY(y);
        Tile.Type tileType = WorldTileManager.getTileAtPosition(x, y).getType();
        return !(tileType.equals(Tile.Type.BLOCK));
    }

    protected boolean testRight() {
        int x = (int) ((position.x / 16) + 0.5f);
        int y;
        if (position.y < 0) y = (int) (position.y / 16) - 1;
        else y = (int) (position.y / 16);
        if (x > (1024 / 16f) - 0.5f) return false;
        x = WorldTileManager.verifyX(x);
        y = WorldTileManager.verifyY(y);
        Tile.Type tileType = WorldTileManager.getTileAtPosition(x, y).getType();
        return !(tileType.equals(Tile.Type.BLOCK));
    }

    protected boolean testDown() {
        int x = (int) (position.x / 16f);
        float y_ = (position.y / 16f) - 2f;
        int y = (int) Math.ceil(y_);
        x = WorldTileManager.verifyX(x);
        y = WorldTileManager.verifyY(y);
        Tile.Type tileType;
        try {
            tileType = WorldTileManager.getTileAtPosition(x, y).getType();
        } catch (Exception e) {
            tileType = Tile.Type.AIR;
        }
        boolean down = !tileType.equals(Tile.Type.BLOCK);
        return down;
    }

    protected final void coolDown() {
        coolDown++;
    }

}
