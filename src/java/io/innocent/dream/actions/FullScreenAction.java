package io.innocent.dream.actions;

import io.innocent.dream.util.DisplayManager;
import io.innocent.dream.util.Settings;

public class FullScreenAction extends Action {

    private boolean ready = false;
    private boolean fullscreen = Boolean.parseBoolean((String) Settings.getSetting("fullscreen"));

    public FullScreenAction() {
        super("key_fullscreen");
    }

    @Override
    public void preformAction() {
        ready = true;
    }

    @Override
    public void afterActionPreformed() {
        if (ready) {
            fullscreen = Boolean.parseBoolean((String) Settings.getSetting("fullscreen"));
            System.out.println("Switching Window Modes");
            if (fullscreen) {
                DisplayManager.goWindowed();
                fullscreen = false;
            } else {
                DisplayManager.goFullScreen();
                fullscreen = true;
            }
            Settings.saveSetting("fullscreen", String.valueOf(fullscreen));
        }
        ready = false;
    }
}
