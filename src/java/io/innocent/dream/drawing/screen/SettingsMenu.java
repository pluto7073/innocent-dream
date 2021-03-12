package io.innocent.dream.drawing.screen;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.drawing.Text;
import io.innocent.dream.drawing.TextureRenderer;
import io.innocent.dream.drawing.button.Button;
import io.innocent.dream.util.DisplayManager;
import io.innocent.dream.util.Settings;

import static org.lwjgl.opengl.GL11.*;

public class SettingsMenu extends AbstractScreen {

    private Button fullscreenBtn;
    private Button scaleBtn;
    private Button controlsBtn;
    private Button backBtn;

    private AbstractScreen previousScreen;

    private final int background = TextureRenderer.loadTexture("assets/title.png");

    private boolean fullscreen = Boolean.parseBoolean((String) Settings.getSetting("fullscreen"));
    private float scale = Float.parseFloat((String) Settings.getSetting("scale"));

    public void setPreviousScreen(AbstractScreen screen) {
        this.previousScreen = screen;
    }

    @Override
    public void prepareToRender() {
        fullscreen = Boolean.parseBoolean((String) Settings.getSetting("fullscreen"));
        scale = Float.parseFloat((String) Settings.getSetting("scale"));

        fullscreenBtn = new Button(Text.ofString("Fullscreen:\n" + fullscreen).withScale(0.25f),
                -48, 48, 64, 32);
        scaleBtn = new Button(Text.ofString("Window Scale:\n" + scale).withScale(0.25f),
                48, 48, 64, 32);
        controlsBtn = new Button(Text.ofString("Controls").withScale(0.25f),
                -48, 0, 64, 32);
        backBtn = new Button(Text.ofString("Back").withScale(0.25f),
                0, -48, 64, 32);
    }

    @Override
    public void renderScreen() {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, background);
        glBegin(GL_QUADS); {
            glTexCoord2f(0, 0);
            glVertex2f(-256, 128);

            glTexCoord2f(1, 0);
            glVertex2f(256, 128);

            glTexCoord2f(1, 1);
            glVertex2f(256, -128);

            glTexCoord2f(0, 1);
            glVertex2f(-256, -128);
        } glEnd();
        glDisable(GL_TEXTURE_2D);
        fullscreenBtn.testButtonPress(this::toggleFullscreen);
        scaleBtn.testButtonPress(this::toggleScale);
        controlsBtn.testButtonPress(this::controls);
        backBtn.testButtonPress(this::back);
        fullscreenBtn.drawButton();
        scaleBtn.drawButton();
        controlsBtn.drawButton();
        backBtn.drawButton();
    }

    public void toggleFullscreen(Button button) {
        fullscreen = !fullscreen;
        Settings.saveSetting("fullscreen", String.valueOf(fullscreen));
        if (fullscreen) {
            DisplayManager.goFullScreen();
        } else {
            DisplayManager.goWindowed();
        }
        button.setText(Text.ofString("Fullscreen:\n" + fullscreen).withScale(0.25f));
    }

    public void toggleScale(Button button) {
        scale += 0.2f;
        if (scale > 2.1f) scale = 0.2f;
        Settings.saveSetting("scale", String.valueOf(scale));
        button.setText(Text.ofString("Window Scale:\n" + scale).withScale(0.25f));
        if (!fullscreen) DisplayManager.goWindowed();
    }

    public void controls(Button button) {
        InnocentDream.CURRENT_SCREEN = InnocentDream.CONTROLS_MENU;
    }

    public void back(Button button) {
        InnocentDream.CURRENT_SCREEN = previousScreen;
    }

}
