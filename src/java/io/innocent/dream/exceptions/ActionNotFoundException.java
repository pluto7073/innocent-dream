package io.innocent.dream.exceptions;

public class ActionNotFoundException extends Exception {

    public ActionNotFoundException(String unkAct) {
        super("Could not find action: " + unkAct);
    }
}
