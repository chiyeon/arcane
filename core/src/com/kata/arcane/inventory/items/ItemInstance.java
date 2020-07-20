package com.kata.arcane.inventory.items;

public class ItemInstance {
    public Item item;
    public int quantity;

    public ItemInstance(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public ItemInstance(Item item) {
        this.item = item;
        this.quantity = 1;
    }
}
