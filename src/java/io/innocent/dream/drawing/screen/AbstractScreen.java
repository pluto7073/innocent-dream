package io.innocent.dream.drawing.screen;

public abstract class AbstractScreen {

    public AbstractScreen() {
        prepareToRender();
    }

    public abstract void prepareToRender();
    public abstract void renderScreen();

}
