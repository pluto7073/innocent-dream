package io.innocent.dream.util;

import io.innocent.dream.InnocentDream;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.util.Properties;

public class Settings {

    private static final Properties SETTINGS;

    static {
        SETTINGS = new Properties();
    }

    /**
     * Initializes the <code>SETTINGS</code>
     * field and loads all settings from the
     * <code>game.properties</code> file if it
     * exists. If it doesn't, then it creates
     * a new file and calls <code>resetProperties()</code>
     */
    public static void start() {
        File settings = new File(InnocentDream.path + "\\game.properties");
        if (settings.exists()) {
            try (FileReader reader = new FileReader(settings)) {
                SETTINGS.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                settings.createNewFile();
                resetProperties();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the current settings stored in <code>SETTINGS</code>
     * in the <code>game.properties</code> file.
     */
    public static void stop() {
        File settings = new File(InnocentDream.path + "\\game.properties");
        try (FileWriter writer = new FileWriter(settings)) {
            SETTINGS.store(writer, "Innocent Dream's Config File");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is used if you try to load
     * a property that doesn't exist.
     * If that happens then the game
     * assumes you don't have the correct
     * <code>game.properties</code> file
     * and re-saves all settings.
     */
    private static void resetProperties() {
        SETTINGS.put("fullscreen", "true");
        SETTINGS.put("scale", "1.0");
        SETTINGS.put("key_destroy_block", String.valueOf(GLFW.GLFW_MOUSE_BUTTON_1));
        SETTINGS.put("key_place_block", String.valueOf(GLFW.GLFW_MOUSE_BUTTON_2));
        SETTINGS.put("key_fullscreen", String.valueOf(GLFW.GLFW_KEY_F5));
        SETTINGS.put("key_left", String.valueOf(GLFW.GLFW_KEY_A));
        SETTINGS.put("key_right", String.valueOf(GLFW.GLFW_KEY_D));
        SETTINGS.put("key_jump", String.valueOf(GLFW.GLFW_KEY_SPACE));
        SETTINGS.put("key_pause", String.valueOf(GLFW.GLFW_KEY_ESCAPE));
        SETTINGS.put("key_screenshot", String.valueOf(GLFW.GLFW_KEY_F2));
        SETTINGS.put("key_inventory", String.valueOf(GLFW.GLFW_KEY_E));
    }

    /**
     * Changes a setting using the <code>setting</code>
     * and <code>value</code>
     * @param setting The Setting to change
     * @param value The value to set the setting to
     */
    public static void saveSetting(String setting, Object value) {
        SETTINGS.put(setting, value);
    }

    public static Object getSetting(String setting) {
        Object value = SETTINGS.get(setting);
        if (value == null) {
            resetProperties();
            value = SETTINGS.get(setting);
        }
        return value;
    }

}
