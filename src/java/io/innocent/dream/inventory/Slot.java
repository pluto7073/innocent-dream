package io.innocent.dream.inventory;

import java.io.Serializable;

import io.innocent.dream.item.Item;

public class Slot implements Serializable {

	private static final long serialVersionUID = -3496862584765003573L;
	private Item item;
    private int count;

    private Slot(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public static Slot of(Item item, int count) {
        return new Slot(item, count);
    }

}
