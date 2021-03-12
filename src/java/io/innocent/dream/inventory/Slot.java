package io.innocent.dream.inventory;

import io.innocent.dream.item.Item;

public class Slot {

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
