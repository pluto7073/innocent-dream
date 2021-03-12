package io.innocent.dream.drawing.button;

import io.innocent.dream.drawing.Text;
import io.innocent.dream.drawing.Texture;
import io.innocent.dream.util.DisplayManager;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Button {

    private static final Texture BUTTON_TEXTURE = new Texture("assets/gui/button.png", 0, 0, 64, 32);
    private static final Texture BUTTON_TEXTURE_SELECTED = new Texture("assets/gui/button.png", 0, 32, 64, 32);

    private Text text;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private Texture currentTex;

    private final String actionStr;

    public Button(Text text, int x, int y, int width, int height) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = Math.max(width, text.getWidth());
        this.height = height;
        currentTex = BUTTON_TEXTURE;
        actionStr = "";
    }

    public Button(Text text, int x, int y, int width, int height, String actionStr) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = Math.max(width, text.getWidth());
        this.height = height;
        currentTex = BUTTON_TEXTURE;
        this.actionStr = actionStr;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public void drawButton() {
        glfwMakeContextCurrent(DisplayManager.win);
        int texID = currentTex.getTextureID();
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texID);
        int width = this.width / 2;
        int height = this.height / 2;
        int startX = 0;
        float endX = 6 / 64f;
        float beginX = 58 / 64f;
        float stopX = 1;
        //Middle
        glBegin(GL_QUADS); {
            glTexCoord2f(endX, 0);
            glVertex2f(-width + x, height + y);
            glTexCoord2f(beginX, 0);
            glVertex2f(width + x, height + y);
            glTexCoord2f(beginX, 1);
            glVertex2f(width + x, -height + y);
            glTexCoord2f(endX, 1);
            glVertex2f(-width + x, -height + y);
        } glEnd();
        //Beginning
        glBegin(GL_QUADS); {
        	glTexCoord2f(startX, 0);
        	glVertex2f(-width + x, height + y);
        	glTexCoord2f(endX, 0);
        	glVertex2f(-width + 6 + x, height + y);
        	glTexCoord2f(endX, 1);
        	glVertex2f(-width + 6 + x, -height + y);
        	glTexCoord2f(startX, 1);
        	glVertex2f(-width + x, -height + y);
        } glEnd();
        //Ending
        glBegin(GL_QUADS); {
        	glTexCoord2f(beginX, 0);
        	glVertex2f(width - 6 + x, height + y);
        	glTexCoord2f(stopX, 0);
        	glVertex2f(width + x, height + y);
        	glTexCoord2f(stopX, 1);
        	glVertex2f(width + x, -height + y);
        	glTexCoord2f(beginX, 1);
        	glVertex2f(width - 6 + x, -height + y);
        } glEnd();
        glDisable(GL_TEXTURE_2D);
        text.render(x - (text.getWidth() / 2), y);
    }

    public String getActionStr() {
        return actionStr;
    }

    boolean clicked = false;
    public void testButtonPress(ButtonAction action) {

        float[] XY = DisplayManager.getMousePositionRelativeToScreen();
        float x = XY[0];
        float y = XY[1];
        boolean touching = (x >= this.x - width / 2f) && (x <= this.x + width / 2f) && (y >= this.y - height / 2f) && (y <= this.y + height / 2f);
        if (touching) {
            if (glfwGetMouseButton(DisplayManager.win, GLFW_MOUSE_BUTTON_1) == GLFW_TRUE) {
                clicked = true;
            } else {
                if (clicked) action.buttonClicked(this);
                clicked = false;
            }
            currentTex = BUTTON_TEXTURE_SELECTED;
        } else {
            currentTex = BUTTON_TEXTURE;
        }
        if (glfwGetMouseButton(DisplayManager.win, GLFW_MOUSE_BUTTON_1) == GLFW_FALSE) {
            clicked = false;
        }
    }

}
