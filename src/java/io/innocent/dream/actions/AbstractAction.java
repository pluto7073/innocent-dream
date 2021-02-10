package io.innocent.dream.actions;

public abstract class AbstractAction {

    protected final int key;

    public AbstractAction(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public abstract void preformAction();

    public void afterActionPreformed() {}

}
