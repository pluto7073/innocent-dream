package io.innocent.dream.exceptions;

public class ActionNotFoundException extends Exception {

	private static final long serialVersionUID = 8998779242745393159L;

	public ActionNotFoundException(String unkAct) {
        super("Could not find action: " + unkAct);
    }
}
