package io.innocent.dream.drawing;

import io.innocent.dream.InnocentDream;

import java.io.IOException;
import java.io.InputStream;

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

    protected void generateTexture() throws IOException {
        textureID = TextureRenderer.loadTexture(texturePath);
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
