package io.innocent.dream.drawing.screen;

import io.innocent.dream.actions.Actions;
import io.innocent.dream.actions.ScreenshotAction;
import io.innocent.dream.drawing.Text;
import io.innocent.dream.entities.Entities;
import io.innocent.dream.entities.Particles;
import io.innocent.dream.item.Items;
import io.innocent.dream.tile.Tiles;
import io.innocent.dream.worldBuilder.WorldBuilder;
import io.innocent.dream.worldBuilder.WorldTileManager;

public class MainGameScreen extends AbstractScreen {

    private Text playerYVal;
    private int worldInt;
    
    public MainGameScreen(int worldInt) {
    	this.worldInt = worldInt;
    	prepareToRender();
    }

    @Override
    public void prepareToRender() {
        Entities.init();
        Tiles.init();
        WorldBuilder.init(worldInt);
        Actions.init();
        Particles.init();
        if (!WorldBuilder.loadedFromFile) {
            WorldBuilder.generateWorld(worldInt);
        }
        playerYVal = Text.ofString("Y").withScale(0.25f);
    }

    @Override
    public void renderScreen() {
        Actions.pollActions();
        WorldTileManager.renderWorld();
        Items.renderRenderedItems();
        Entities.renderEntities();
        Particles.renderParticles();
        ScreenshotAction.i++;
        if (ScreenshotAction.shouldShowText) {
            Text text = Text.ofString("Saved Screenshot").withScale(0.25f);
            text.render((int) (-(text.getWidth() / 2) + Entities.PLAYER.getPosition().x), (int) (-48 + Entities.PLAYER.getPosition().y));
        }
        if (ScreenshotAction.i > 1000) {
            ScreenshotAction.shouldShowText = false;
        }
        int x = (int) Math.max(Entities.PLAYER.getPosition().x, WorldTileManager.MIN_CAM_X);
        x = Math.min(x, WorldTileManager.MAX_CAM_X);
        playerYVal.setString("Player Health: " + Entities.PLAYER.getHp());
        playerYVal.render(-208 + x, 80 + (int) Entities.PLAYER.getPosition().y);
        WorldBuilder.world.tick();
    }
}
