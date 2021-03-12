package io.innocent.dream.inventory;

import io.innocent.dream.drawing.Texture;
import org.joml.Vector2i;

public class PlayerInventory extends AbstractInventory {

	private static final long serialVersionUID = -800024787956296348L;

	public PlayerInventory() {
        super(new Vector2i(96, 96), 3, 3,
                new Texture("assets/gui/inventory.png"), 21,
                new Vector2i(9, 12), new Vector2i(3, 3));
    }

}
