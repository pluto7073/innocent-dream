package io.innocent.dream;

import io.innocent.dream.crash_report.CrashReport;
import io.innocent.dream.drawing.screen.*;
import io.innocent.dream.util.*;
import io.innocent.dream.worldBuilder.WorldSaver;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Map;

import static io.innocent.dream.util.DisplayManager.win;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class InnocentDream implements Runnable {

    public static GameAPIManager apiManager;
    public static String version = "v0.1.4";
    public static Timer timer;
    public static LogOutput logOutput;
    public static String path;
    public static CrashReport crashReportBuilder;

    public static AbstractScreen CURRENT_SCREEN;
    public static MainGameScreen MAIN_GAME;
    public static PauseMenu PAUSE_MENU;
    public static SettingsMenu SETTINGS_MENU;
    public static MainMenu MAIN_MENU;
    public static ControlsMenu CONTROLS_MENU;
    public static LogoScreen LOGO_SCREEN;
    public static WorldScreen WORLD_SCREEN;

    public static float width = 256;
    public static float maxScroll = 768;

    public static void calculateHeightScale() {
        IntBuffer buffer = BufferUtils.createIntBuffer(1);
        IntBuffer buffer1 = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(win, buffer, buffer1);
        int width = buffer.get(0);
        int height = buffer1.get(0);
        float scale = 256f / height;
        float wide = width * scale;
        InnocentDream.width = wide / 2;
        System.out.println(InnocentDream.width);
        System.out.println(maxScroll);
    }

    @Override
    public void run() {
    	DisplayManager.create();
        calculateHeightScale();
        glfwMakeContextCurrent(win);
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(-width, width,
                -128, 128,
                -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        PAUSE_MENU = new PauseMenu();
        SETTINGS_MENU = new SettingsMenu();
        MAIN_MENU = new MainMenu();
        CONTROLS_MENU = new ControlsMenu();
        LOGO_SCREEN = new LogoScreen();
        WORLD_SCREEN = new WorldScreen();
        CURRENT_SCREEN = LOGO_SCREEN;
        while (!GLFW.glfwWindowShouldClose(win)) {
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT);

            try {
                CURRENT_SCREEN.renderScreen();
            } catch (Exception e) {
                if (CURRENT_SCREEN == MAIN_GAME)
                    WorldSaver.saveWorld();
                destroy();
                System.err.println("For more info go to: " + crashReportBuilder
                    .createCrashReport(e));
                System.exit(e.hashCode());
            }

            glfwSwapBuffers(win);

            timer.updateTime();
        }
        destroy();
    }

    public static InputStream getResAsStream(String path) throws FileNotFoundException {
        ClassLoader loader = InnocentDream.class.getClassLoader();
        InputStream in = loader.getResourceAsStream(path);
        if (in == null) {
            throw new FileNotFoundException(path);
        }
        return in;
    }

    public static void init() {
        
    }

    public void destroy() {
        glfwDestroyWindow(win);
        apiManager.stop();
        Settings.stop();
    }

    public static void main(String[] args) {
        LibraryManager.testLibs();
        File file = new File("libs\\natives");
        System.setProperty("org.lwjgl.librarypath",
                file.getAbsolutePath());
    	logOutput = new LogOutput();
        System.setOut(logOutput);
        timer = new Timer();
        crashReportBuilder = new CrashReport(false);
        Map<String, String> env = System.getenv();
        if (env.containsKey("OneDrive")) {
            path = env.get("OneDrive") + "\\Documents\\My Games\\Innocent Dream";
        } else {
            path = env.get("USERPROFILE") + "\\Documents\\My Games\\Innocent Dream";
        }
        System.out.println(path);
        new File(path).mkdirs();
        new File(path + "\\worlds").mkdirs();
        new Thread(new InnocentDream()).start();
    }

}
