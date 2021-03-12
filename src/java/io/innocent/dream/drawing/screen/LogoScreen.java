package io.innocent.dream.drawing.screen;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.util.DisplayManager;
import io.innocent.dream.util.GameAPIManager;
import io.innocent.dream.drawing.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class LogoScreen extends AbstractScreen {

    private Texture plutoGameTex;
    private Text loadingText;
    private int i = 0;

    @Override
    public void prepareToRender() {
        plutoGameTex = new Texture("assets/pluto-games.png");
        loadingText = Text.ofString(" Loading...");
    }

    @Override
    public void renderScreen() {
        glfwMakeContextCurrent(DisplayManager.win);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-InnocentDream.width, InnocentDream.width, -128, 128, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glClearColor(0, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, plutoGameTex.getTextureID());
        glBegin(GL_QUADS); {
            glTexCoord2f(0, 0);
            glVertex2f(-64, 64);
            glTexCoord2f(1, 0);
            glVertex2f(64, 64);
            glTexCoord2f(1, 1);
            glVertex2f(64, -64);
            glTexCoord2f(0, 1);
            glVertex2f(-64, -64);
        } glEnd();
        glDisable(GL_TEXTURE_2D);
        loadingText.render(-((loadingText.getWidth() - 48) / 2), -86);
        if (i == 5) {
            InnocentDream.apiManager = new GameAPIManager();
        } else if (i > 120) {
        	InnocentDream.CURRENT_SCREEN = InnocentDream.MAIN_MENU;
        }
        i++;
    }

}
