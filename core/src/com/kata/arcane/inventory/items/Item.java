package com.kata.arcane.inventory.items;

import com.badlogic.gdx.graphics.Texture;
import com.kata.arcane.Enums.*;

public abstract class Item {
    public int id;
    public String itemName, itemDescription;
    public Texture itemSprite;
    public ItemType itemType;
    public boolean itemStackable;

    public Item(int id, String name, String description, String spritePath, ItemType type, boolean isStackable) {
        this.id = id;
        itemName = name;
        itemDescription = description;
        itemSprite = new Texture(spritePath);
        itemType = type;
        itemStackable = isStackable;
    }

    public abstract void Use();
}
