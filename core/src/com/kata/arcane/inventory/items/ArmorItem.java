package com.kata.arcane.inventory.items;

import com.kata.arcane.Enums.*;

public class ArmorItem extends Item {
    public int armorValue;
    public ArmorType armorType;

    public ArmorItem(int id, String name, String description, String spritePath, boolean isStackable, int armorValue, ArmorType armorType) {
        super(id, name, description, spritePath, ItemType.ARMOR, isStackable);
        this.armorValue = armorValue;
        this.armorType = armorType;

    }

    @Override
    public void Use() {

    }
}
