package com.kata.arcane.inventory.items;

import com.kata.arcane.Enums.*;

public class TileItem extends Item {
    public TileType tileType;

    public TileItem(int id, String name, String description, String spritePath, boolean isStackable, TileType tileType) {
        super(id, name, description, spritePath, ItemType.TILE, isStackable);
        this.tileType = tileType;
    }

    @Override
    public void Use() {

    }
}
