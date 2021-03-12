package io.innocent.dream.drawing.screen;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.drawing.Text;
import io.innocent.dream.drawing.TextureRenderer;
import io.innocent.dream.drawing.button.Button;
import io.innocent.dream.util.DisplayManager;
import io.innocent.dream.util.KeyCodes;
import io.innocent.dream.util.Settings;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.opengl.GL11.*;

public class ControlsMenu extends AbstractScreen {

    private Button leftCtrlBtn;
    private Button rightCtrlBtn;
    private Button jumpBtn;
    private Button destroy;
    private Button place;
    private Button screenshot;
    private Button pause;
    private Button fullscreen;

    private Button backBtn;

    private final int background = TextureRenderer.loadTexture("assets/title.png");

    @Override
    public void prepareToRender() {
        leftCtrlBtn = new Button(Text.ofString("Left:").withScale(0.25f),
                -48, 80, 64, 16, "key_left");
        rightCtrlBtn = new Button(Text.ofString("Right").withScale(0.25f),
                -48, 48, 64, 16, "key_right");
        jumpBtn = new Button(Text.ofString("Jump:").withScale(0.25f),
                -48, 16, 64, 16, "key_jump");
        destroy = new Button(Text.ofString("Destroy:").withScale(0.25f),
                -48, -16, 64, 16, "key_destroy_block");
        place = new Button(Text.ofString("Place:").withScale(0.25f),
                48, 80, 64, 16, "key_place_block");
        screenshot = new Button(Text.ofString("Screenshot:").withScale(0.25f),
                48, 48, 64, 16, "key_screenshot");
        pause = new Button(Text.ofString("Pause:").withScale(0.25f),
                48, 16, 64, 16, "key_pause");
        fullscreen = new Button(Text.ofString("Fullscreen:").withScale(0.25f),
                48, -16, 64, 16, "key_fullscreen");

        backBtn = new Button(Text.ofString("Back").withScale(0.25f),
                0, -48, 64, 32);
    }

    @Override
    public void renderScreen() {
        leftCtrlBtn.setText(Text.ofString("Left:" + getKeyString(
                Integer.parseInt((String) Settings.getSetting("key_left"))
        )).withScale(0.25f));
        rightCtrlBtn.setText(Text.ofString("Right:" + getKeyString(
                Integer.parseInt((String) Settings.getSetting("key_right"))
        )).withScale(0.25f));
        jumpBtn.setText(Text.ofString("Jump:" + getKeyString(
                Integer.parseInt((String) Settings.getSetting("key_jump"))
        )).withScale(0.25f));
        destroy.setText(Text.ofString("Destroy:" + getKeyString(
                Integer.parseInt((String) Settings.getSetting("key_destroy_block"))
        )).withScale(0.25f));
        place.setText(Text.ofString("Place:" + getKeyString(
                Integer.parseInt((String) Settings.getSetting("key_place_block"))
        )).withScale(0.25f));
        screenshot.setText(Text.ofString("Screenshot:" + getKeyString(
                Integer.parseInt((String)  Settings.getSetting("key_screenshot"))
        )).withScale(0.25f));
        pause.setText(Text.ofString("Pause:" + getKeyString(
                Integer.parseInt((String) Settings.getSetting("key_pause"))
        )).withScale(0.25f));
        fullscreen.setText(Text.ofString("Fullscreen:" + getKeyString(
                Integer.parseInt((String) Settings.getSetting("key_fullscreen"))
        )).withScale(0.25f));
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
        leftCtrlBtn.testButtonPress(this::listen);
        rightCtrlBtn.testButtonPress(this::listen);
        jumpBtn.testButtonPress(this::listen);
        destroy.testButtonPress(this::listen);
        place.testButtonPress(this::listen);
        screenshot.testButtonPress(this::listen);
        pause.testButtonPress(this::listen);
        fullscreen.testButtonPress(this::listen);
        backBtn.testButtonPress(this::back);
        leftCtrlBtn.drawButton();
        rightCtrlBtn.drawButton();
        jumpBtn.drawButton();
        destroy.drawButton();
        place.drawButton();
        screenshot.drawButton();
        pause.drawButton();
        fullscreen.drawButton();
        backBtn.drawButton();
    }

    public void listen(Button button) {
        if (!listening)
            startListening(button.getActionStr());
    }

    public void back(Button button) {
        InnocentDream.CURRENT_SCREEN = InnocentDream.SETTINGS_MENU;
    }

    private boolean listening = false;

    public void startListening(String keyResult) {
        listening = true;
        GLFW.glfwSetKeyCallback(DisplayManager.win, (window, key, scancode, action, mods) -> {
            Settings.saveSetting(keyResult, String.valueOf(key));
            listening = false;
            GLFW.glfwSetKeyCallback(DisplayManager.win, (w, k, s, a, m) -> {});
            GLFW.glfwSetMouseButtonCallback(DisplayManager.win, (a, b, c, d)->{});
        });
        GLFW.glfwSetMouseButtonCallback(DisplayManager.win, (l, i, i1, i2) -> {
            Settings.saveSetting(keyResult, String.valueOf(i));
            listening = false;
            GLFW.glfwSetMouseButtonCallback(DisplayManager.win, (a, b, c, d)->{});
            GLFW.glfwSetKeyCallback(DisplayManager.win, (w, k, s, a, m) -> {});
        });
    }

    private String getKeyString(int key) {
        if (key < GLFW.GLFW_MOUSE_BUTTON_LAST + 1) {
            return "Mouse" + (key + 1);
        }
        return KeyCodes.getKeyName(key);
    }

}
