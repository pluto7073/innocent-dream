package io.innocent.dream.actions;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.registry.Registry;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class Actions {

    public static final AbstractAction SCREENSHOT;

    static {
        SCREENSHOT = Registry.register("screenshot", new ScreenshotAction());
    }

    public static void init(){}

    public static void pollActions() {
        HashMap<String, AbstractAction> actions = Registry.getActionsRegistry();
        actions.forEach((s, abstractAction) -> {
            if (glfwGetKey(InnocentDream.win, abstractAction.getKey()) == GLFW_TRUE)
                abstractAction.preformAction();
            else
                abstractAction.afterActionPreformed();
        });
    }

}
