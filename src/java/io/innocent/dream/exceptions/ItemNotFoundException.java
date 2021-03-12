package io.innocent.dream.exceptions;

public class ItemNotFoundException extends Exception {

	private static final long serialVersionUID = -2128231031866840305L;

	public ItemNotFoundException(String itemID) {
        super("Could not find item with id: " + itemID);
    }

}
