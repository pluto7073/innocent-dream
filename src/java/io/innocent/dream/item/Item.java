package io.innocent.dream.item;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.drawing.Texture;
import io.innocent.dream.util.DisplayManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Scanner;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Item implements Serializable {

	private static final long serialVersionUID = 8806439160390533270L;

	public static final class ItemInfoBuilder {
        private final InputStream infoFile;
        public ItemInfoBuilder(InputStream infoFile) {
            this.infoFile = infoFile;
        }
        public Settings build() throws ParseException {
            Scanner scanner = new Scanner(infoFile);
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                if (!data.equals("\n")) {
                    builder.append(data);
                }
            }
            scanner.close();
            JSONParser parser = new JSONParser();
            JSONObject item = (JSONObject) parser.parse(builder.toString());
            JSONObject itemInfo = (JSONObject) item.get("item");
            JSONArray extraInfo = (JSONArray) itemInfo.get("extraInfo");
            String displayName = (String) itemInfo.get("name");
            int maxCount = Integer.parseInt(String.valueOf(itemInfo.get("max-count")));
            return new Settings(displayName, maxCount, extraInfo);
        }
    }

    public static final class Settings implements Serializable {
		private static final long serialVersionUID = -7235939494304583297L;
		private final String displayName;
        private final int maxCount;
        private final JSONArray extraInfo;
        public Settings(String displayName, int maxCount, JSONArray extraInfo) {
            this.displayName = displayName;
            this.maxCount = maxCount;
            this.extraInfo = extraInfo;
        }
        public String getDisplayName() {
            return displayName;
        }
        public int getMaxCount() {
            return maxCount;
        }
        public JSONArray getExtraInfo() {
            return extraInfo;
        }
    }

    protected Settings settings;
    protected final Texture texture;
    protected final String name;

    public Item(String infoFolder, String name) {
        this.name = name;
        String filePath = infoFolder + "/" + name + ".iditem";
        try {
            InputStream is = InnocentDream.getResAsStream(filePath);
            settings = (new ItemInfoBuilder(is)).build();
        } catch (FileNotFoundException | ParseException e) {
            new ItemInfoParseException(name, filePath).printStackTrace();
            e.printStackTrace();
        }
        for (Object o : settings.getExtraInfo()) {
            if ("untextured".equals(o)) {
                texture = null;
                return;
            }
        }
        texture = new Texture("assets/items/" + name + ".png");
    }

    public String getName() {
        return name;
    }

    public Settings getSettings() {
        return settings;
    }

    public void render(int x, int y, int width, int height) {
        for (Object o : settings.getExtraInfo()) {
            if ("untextured".equals(o)) {
                return;
            }
        }
        width /= 2;
        height /= 2;
        glfwMakeContextCurrent(DisplayManager.win);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        glBegin(GL_QUADS); {
            glTexCoord2f(0, 0);
            glVertex2f(-width + x, height + y);
            glTexCoord2f(1, 0);
            glVertex2f(width + x, height + y);
            glTexCoord2f(1, 1);
            glVertex2f(width + x, -height + y);
            glTexCoord2f(0, 1);
            glVertex2f(-width + x, -height + y);
        } glEnd();
        glDisable(GL_TEXTURE_2D);
    }

}
