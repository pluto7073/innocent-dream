package io.innocent.dream.drawing;

import java.io.IOException;

public class Texture {

    private int textureID;
    private final String texturePath;

    public Texture(String texturePath) {
        this.texturePath = texturePath;
        try {
            generateTexture();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Texture(String texturePath, int x, int y, int w, int h) {
        this.texturePath = texturePath;
        generateTexture(x, y, w, h);
    }

    protected void generateTexture() throws IOException {
        textureID = TextureRenderer.loadTexture(texturePath);
    }

    protected void generateTexture(int x, int y, int w, int h) {
        textureID = TextureRenderer.loadTextureFromSectionOfImage(texturePath, x, y, w, h);
    }

    public int getTextureID() {
        return textureID;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

}
