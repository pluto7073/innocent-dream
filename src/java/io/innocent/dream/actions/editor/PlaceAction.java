package io.innocent.dream.actions.editor;

import org.joml.Vector2f;

import io.innocent.dream.actions.Action;
import io.innocent.dream.entities.Entities;
import io.innocent.dream.tile.Tile;
import io.innocent.dream.util.DisplayManager;
import io.innocent.dream.worldBuilder.WorldTileManager;

public class PlaceAction extends Action {
	
	private boolean ready = false;

    public PlaceAction() {
        super("key_place_block");
    }

    @Override
    public void preformAction() {
    	ready = true;
    }
    
    @Override
    public void afterActionPreformed() {
    	if (ready) {
    		float[] pos = DisplayManager.getMousePositionRelativeToScreen();
            float xv = Entities.PLAYER.getPosition().x;
            xv = Math.max(xv, -768);
            xv = Math.min(xv, 768);
            pos[0] += xv;
            pos[0] /= 16;
            pos[1] += Entities.PLAYER.getPosition().y;
            pos[1] /= 16;
            pos[1] -= 1;
            int x = (int) (pos[0]);
            int y = (int) (pos[1]);
    		Tile tile = WorldTileManager.getTileAtPosition(x, y);
    		if (tile.getType().equals(Tile.Type.AIR)) {
    			//TODO: Get Block in the selected slot and place
    		} else {
    			tile.onUse(new Vector2f(x, y));
    		}
    	}
    	ready = false;
    }

}
