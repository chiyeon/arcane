package com.kata.arcane.inventory.items;

import com.kata.arcane.Enums.*;

public class DefaultItem extends Item {

    public DefaultItem(int id, String name, String description, String spritePath, boolean isStackable) {
        super(id, name, description, spritePath, ItemType.DEFAULT, isStackable);
    }

    @Override
    public void Use() {

    }
}
