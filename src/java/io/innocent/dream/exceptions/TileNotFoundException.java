package io.innocent.dream.exceptions;

public class TileNotFoundException extends Exception {

	private static final long serialVersionUID = 4942580860390819024L;

	public TileNotFoundException(String unkTile) {
        super("Could not find tile: " + unkTile);
    }
}
