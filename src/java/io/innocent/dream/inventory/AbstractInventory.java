package io.innocent.dream.inventory;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.drawing.Text;
import io.innocent.dream.drawing.Texture;
import io.innocent.dream.entities.Entities;
import io.innocent.dream.item.Item;
import io.innocent.dream.item.Items;
import io.innocent.dream.util.DisplayManager;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL11.*;

public abstract class AbstractInventory extends ArrayList<Slot> {

	private static final long serialVersionUID = -8794296307132522368L;
	protected Vector2i size;
    protected int rows;
    protected int columns;
    protected final Texture texture;
    protected int slotSize;
    protected Vector2i offset;
    protected Vector2i spacing;

    protected AbstractInventory(Vector2i size, int rows, int columns, Texture texture,
                                int slotSize, Vector2i offset, Vector2i spacing) {
        this.size = size;
        this.rows = rows;
        this.columns = columns;
        this.texture = texture;
        this.slotSize = slotSize;
        this.offset = offset;
        this.spacing = spacing;
        init();
    }

    public int getSize() {
        return rows * columns;
    }

    public void init() {
        for (int i = 0; i < getSize(); i++) {
            add(Slot.of(Items.AIR_ITEM, 1));
        }
    }

    public void renderInventory() {
        int id = texture.getTextureID();
        int width = size.x / 2;
        int height = size.y / 2;
        Vector2f pos = Entities.PLAYER.getPosition();
        float x;
        x = Math.min(pos.x, InnocentDream.maxScroll);
        x = Math.max(x, -InnocentDream.maxScroll);
        glfwMakeContextCurrent(DisplayManager.win);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, id);
        glBegin(GL_QUADS); {
            glTexCoord2f(0, 0);
            glVertex2f(-width + x, height + pos.y);
            glTexCoord2f(1, 0);
            glVertex2f(width + x, height + pos.y);
            glTexCoord2f(1, 1);
            glVertex2f(width + x, -height + pos.y);
            glTexCoord2f(0, 1);
            glVertex2f(-width + x, -height + pos.y);
        } glEnd();
        glDisable(GL_TEXTURE_2D);

        int i = 0;
        int xo = offset.x + (slotSize / 2);
        int yo = offset.y + (slotSize / 2);
        List<Text> textToDraw = new ArrayList<>();
        for (int row = -xo; row < -(size.y / 2); row += -(getSize() + (spacing.x))) {
            for (int col = yo; col < size.x / 2; col += getSize() + (spacing.y * 5)) {
                Item item = this.get(i).getItem();
                int xi = (int) (col + x - 47);
                int yi = (int) (row + pos.y + 3);
                item.render(xi, yi, slotSize, slotSize);
                int count = this.get(i).getCount();
                if (count > 1) {
                    Text t = new Text(String.valueOf(count), 0.25f, new Vector2f(xi, yi));
                    xi += slotSize / 2;
                    yi -=  slotSize / 2;
                    t.setPosition(new Vector2f(xi, yi));
                    textToDraw.add(t);
                }
                i++;
            }
        }
        textToDraw.forEach(Text::render);
    }

    public Slot getSlot(int index) {
        return this.get(index);
    }

    public void setItemAtIndex(Item item, int index) {
        Slot slot = this.get(index);
        slot.setItem(item);
        slot.setCount(1);
        this.set(index, slot);
    }

    public void setCountAtIndex(int count, int index) {
        Slot slot = this.get(index);
        slot.setCount(count);
        this.set(index, slot);
    }

    public void setItemWithCountAtIndex(Item item, int count, int index) {
        Slot slot = this.get(index);
        slot.setItem(item);
        slot.setCount(count);
        this.set(index, slot);
    }

    public void addItem(Item item) {
        for (int i = 0; i < getSize(); i++) {
            Slot s = get(i);
            Item ite = s.getItem();
            if (ite.equals(Items.AIR_ITEM)) {
                s.setItem(item);
                set(i, s);
                break;
            } else if ((ite.equals(item)) &&
                    (s.getCount() <= ite.getSettings().getMaxCount())) {
                s.setCount(s.getCount() + 1);
                set(i, s);
                break;
            }
        }
    }
    
    public JSONArray toJSONArray() {
    	JSONArray array = new JSONArray();
    	for (Slot s : this) {
    		JSONObject o = new JSONObject();
    		o.put("item", s.getItem().getName());
    		o.put("count", s.getCount());
    		array.add(o);
    	}
    	return array;
    }

}