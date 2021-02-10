package io.innocent.dream.exceptions;

public class TileNotFoundException extends Exception {

    public TileNotFoundException(String unkTile) {
        super("Could not find tile: " + unkTile);
    }
}
