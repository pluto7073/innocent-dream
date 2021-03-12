package io.innocent.dream.drawing;

import io.innocent.dream.util.DisplayManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL11.*;

public class Text {

    public static final int WIDTH = 16;
    public static final int HEIGHT = 16;

    private char[] chars;
    private float scale;
    private Vector2f position;
    private Vector3f colour;

    public Text(char[] chars, float scale) {
        this.chars = chars;
        this.scale = scale;
        this.position = new Vector2f(0, 0);
        this.colour = new Vector3f(255, 255, 255);
    }

    public Text(String src, float scale, Vector2f position) {
        this.chars = src.toCharArray();
        this.scale = scale;
        this.position = position;
        this.colour = new Vector3f(255, 255, 255);
    }
    
    public void setColour(Vector3f colour) {
    	this.colour = colour;
    }

    public void setString(String s) {
        chars = s.toCharArray();
    }

    public void setChars(char[] chars) {
        this.chars = chars;
    }

    public static Text ofString(String s) {
        return new Text(s.toCharArray(), 1f);
    }

    public Text withScale(float scale) {
        this.scale = scale;
        return this;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public int getWidth() {
        int length = 0;
        for (length = -1; length < chars.length - 1; length++ ) {
            char c = chars[length + 1];
            if (c == '\n') break;
        }
        return (int) ((length + 1) * WIDTH * scale);
    }

    public int getHeight() {
        int height = HEIGHT;
        for (char c : chars) {
            if (c == '\n') height += HEIGHT;
        }
        return (int) (height * scale);
    }

    public void render(int x, int y) {
        int w = (int) ((WIDTH / 2) * scale);
        int h = (int) ((HEIGHT / 2) * scale);
        int drawX = x;
        glfwMakeContextCurrent(DisplayManager.win);
        glColor4f(colour.x / 255, colour.y / 255, colour.z / 255, 1);
        for (char aChar : chars) {
            if (aChar == '\n') {
                y -= HEIGHT * scale;
                drawX = x;
                continue;
            }
            Texture charTex = TextureRenderer.getCharacter(aChar);
            glMatrixMode(GL_TEXTURE);
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_ALPHA);
            glEnable(GL_BLEND);
            glEnable(GL_DEPTH);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glPushMatrix();
            glBindTexture(GL_TEXTURE_2D, charTex.getTextureID());
            glBegin(GL_QUADS); {
                glTexCoord2f(0, 0);
                glVertex2f(-w + drawX, h + y);

                glTexCoord2f(1, 0);
                glVertex2f(w + drawX, h + y);

                glTexCoord2f(1, 1);
                glVertex2f(w + drawX, -h + y);

                glTexCoord2f(0, 1);
                glVertex2f(-w + drawX, -h + y);
            } glEnd();
            glDisable(GL_TEXTURE_2D);
            drawX += WIDTH * scale;
        }
        glColor4f(1, 1, 1, 1);
    }

    public void render() {
        int x = (int) position.x;
        int y = (int) position.y;
        int w = (int) ((WIDTH / 2) * scale);
        int h = (int) ((HEIGHT / 2) * scale);
        int drawX = x;
        glfwMakeContextCurrent(DisplayManager.win);
        for (char aChar : chars) {
            if (aChar == '\n') {
                y -= HEIGHT * scale;
                drawX = x;
                continue;
            }
            Texture charTex = TextureRenderer.getCharacter(aChar);
            glMatrixMode(GL_TEXTURE);
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_ALPHA);
            glEnable(GL_BLEND);
            glEnable(GL_DEPTH);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glPushMatrix();
            glBindTexture(GL_TEXTURE_2D, charTex.getTextureID());
            glBegin(GL_QUADS); {
                glTexCoord2f(0, 0);
                glVertex2f(-w + drawX, h + y);

                glTexCoord2f(1, 0);
                glVertex2f(w + drawX, h + y);

                glTexCoord2f(1, 1);
                glVertex2f(w + drawX, -h + y);

                glTexCoord2f(0, 1);
                glVertex2f(-w + drawX, -h + y);
            } glEnd();
            glDisable(GL_TEXTURE_2D);
            drawX += WIDTH * scale;
        }
    }

}
