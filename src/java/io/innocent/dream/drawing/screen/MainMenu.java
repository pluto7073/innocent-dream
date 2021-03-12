package io.innocent.dream.drawing.screen;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.actions.Actions;
import io.innocent.dream.auth.AccountLogin;
import io.innocent.dream.drawing.Text;
import io.innocent.dream.drawing.TextureRenderer;
import io.innocent.dream.drawing.button.Button;
import io.innocent.dream.util.DisplayManager;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Vector3f;

public class MainMenu extends AbstractScreen {

    private Button playBtn;
    private Button settingsBtn;
    private Button quitBtn;
    private Button loginBtn;
    private Text titleText;

    private int background;

    @Override
    public void prepareToRender() {
        playBtn = new Button(Text.ofString("Play").withScale(0.25f),
                0, 48, 64, 32);
        settingsBtn = new Button(Text.ofString("Settings").withScale(0.25f),
                0, 0, 64, 32);
        quitBtn = new Button(Text.ofString("Quit").withScale(0.25f),
                0, -48, 64, 32);
        loginBtn = new Button(Text.ofString("Login using GameJolt").withScale(0.25f),
                -200, -100, 64, 32);
        background = TextureRenderer.loadTexture("assets/title.png");
        titleText = Text.ofString("Innocent Dream");
    }

    @Override
    public void renderScreen() {
        if (InnocentDream.apiManager.isLoggedIn()) {
            loginBtn.setText(Text.ofString("Logged in as \n" + InnocentDream.apiManager.getUsername()).withScale(0.25f));
        } else {
            loginBtn.setText(Text.ofString("Login using \nGameJolt").withScale(0.25f));
        }
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(-256, 256, -128, 128, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        if (glfwGetKey(DisplayManager.win, GLFW_KEY_F2) == GLFW_TRUE) {
            Actions.SCREENSHOT.preformAction();
        } else {
            Actions.SCREENSHOT.afterActionPreformed();
        }
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
        glDisable(GL_TEXTURE_2D);
        playBtn.testButtonPress(this::play);
        settingsBtn.testButtonPress(this::settings);
        quitBtn.testButtonPress(this::quit);
        if (!InnocentDream.apiManager.isLoggedIn()) {
            loginBtn.testButtonPress(this::login);
        }
        playBtn.drawButton();
        settingsBtn.drawButton();
        quitBtn.drawButton();
        loginBtn.drawButton();
        titleText.setColour(new Vector3f(0, 0, 0));
        titleText.render(-(titleText.getWidth() / 2) + 2, 98);
        titleText.setColour(new Vector3f(255, 255, 255));
        titleText.render(-(titleText.getWidth() / 2), 100);
    }

    public void play(Button button) {
    	InnocentDream.WORLD_SCREEN = new WorldScreen();
        InnocentDream.CURRENT_SCREEN = InnocentDream.WORLD_SCREEN;
    }

    public void settings(Button button) {
        InnocentDream.SETTINGS_MENU.setPreviousScreen(this);
        InnocentDream.CURRENT_SCREEN = InnocentDream.SETTINGS_MENU; 
    }

    public void quit(Button button) {
        glfwSetWindowShouldClose(DisplayManager.win, true);
    }

    public void login(Button button) {
        DisplayManager.goWindowed();
        new AccountLogin();
    }

}
