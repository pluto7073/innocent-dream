package io.innocent.dream.tile;

import io.innocent.dream.drawing.Texture;

public class AirTile extends Tile {

    public AirTile() {
        super(Type.AIR, new Texture("assets/unknown_texture.png"));
    }

    @Override
    public void renderTileAtPos(float x, float y) {}

}
