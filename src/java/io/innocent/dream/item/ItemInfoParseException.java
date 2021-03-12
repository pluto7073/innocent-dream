package io.innocent.dream.item;

public class ItemInfoParseException extends Exception {

	private static final long serialVersionUID = 11121361532240526L;

	public ItemInfoParseException(String itemName, String infoPath) {
        super("Failed to parse Item: " + itemName + " using the file " + infoPath);
    }

}
