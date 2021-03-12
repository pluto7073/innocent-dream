package io.innocent.dream.actions;

import io.innocent.dream.actions.direction.JumpAction;
import io.innocent.dream.actions.direction.LeftAction;
import io.innocent.dream.actions.direction.RightAction;
import io.innocent.dream.actions.editor.DestroyAction;
import io.innocent.dream.registry.Registry;
import io.innocent.dream.util.DisplayManager;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class Actions {

    public static final Action SCREENSHOT;
    public static final Action MOVE_LEFT;
    public static final Action MOVE_RIGHT;
    public static final Action JUMP;
    public static final Action PAUSE;
    public static final Action FULLSCREEN;
    public static final Action DESTROY;
    public static final Action INVENTORY;

    static {
        SCREENSHOT = Registry.register("screenshot", new ScreenshotAction());
        MOVE_LEFT = Registry.register("left", new LeftAction());
        MOVE_RIGHT = Registry.register("right", new RightAction());
        JUMP = Registry.register("jump", new JumpAction());
        PAUSE = Registry.register("pause", new PauseAction());
        FULLSCREEN = Registry.register("fullscreen", new FullScreenAction());
        DESTROY = Registry.register("destroy", new DestroyAction());
        INVENTORY = Registry.register("inventory", new ToggleInventoryAction());
    }

    public static void init(){}

    public static void pollActions() {
        HashMap<String, Action> actions = Registry.getActionsRegistry();
        actions.forEach((s, abstractAction) -> {
            if (glfwGetKey(DisplayManager.win, abstractAction.getKey()) == GLFW_TRUE)
                abstractAction.preformAction();
            else if(glfwGetMouseButton(DisplayManager.win, abstractAction.getKey()) == GLFW_TRUE) {
                abstractAction.preformAction();
            } else {
                abstractAction.afterActionPreformed();
            }
        });
    }

}
