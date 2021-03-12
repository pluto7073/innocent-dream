package io.innocent.dream.drawing.screen;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.actions.Actions;
import io.innocent.dream.drawing.Text;
import io.innocent.dream.drawing.button.Button;
import io.innocent.dream.util.DisplayManager;
import io.innocent.dream.worldBuilder.WorldSaver;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class PauseMenu extends AbstractScreen {

    private Button resumeBtn;
    private Button quitBtn;
    private Button settingsBtn;

    private int background;

    public void setBackground(int ID) {
        background = ID;
    }

    @Override
    public void prepareToRender() {
        resumeBtn = new Button(Text.ofString("Resume").withScale(0.25f),
                0, 48, 64, 32);
        quitBtn = new Button(Text.ofString("Quit").withScale(0.25f),
                0, -48, 64, 32);
        settingsBtn = new Button(Text.ofString("Settings").withScale(0.25f),
                0, 0, 64, 32);
    }

    @Override
    public void renderScreen() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(-256, 256, -128, 128, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        glfwMakeContextCurrent(DisplayManager.win);
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
        if (glfwGetKey(DisplayManager.win, GLFW_KEY_F2) == GLFW_TRUE) {
            Actions.SCREENSHOT.preformAction();
        } else {
            Actions.SCREENSHOT.afterActionPreformed();
        }
        resumeBtn.testButtonPress(this::resume);
        settingsBtn.testButtonPress(this::settings);
        quitBtn.testButtonPress(this::quit);
        resumeBtn.drawButton();
        settingsBtn.drawButton();
        quitBtn.drawButton();
    }

    public void resume(Button button) {
        InnocentDream.CURRENT_SCREEN = InnocentDream.MAIN_GAME;
    }

    public void settings(Button button) {
        InnocentDream.SETTINGS_MENU.setPreviousScreen(this);
        InnocentDream.CURRENT_SCREEN = InnocentDream.SETTINGS_MENU;
    }

    public void quit(Button button) {
        WorldSaver.saveWorld();
        InnocentDream.MAIN_GAME = null;
        InnocentDream.CURRENT_SCREEN = InnocentDream.MAIN_MENU;
    }

}
