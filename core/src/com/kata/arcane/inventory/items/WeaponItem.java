package com.kata.arcane.inventory.items;

import com.kata.arcane.Enums.*;

public class WeaponItem extends Item {
    public int attackDamage;
    public float attackSpeed;

    public WeaponItem(int id, String name, String description, String spritePath, int attackDamage, float attackSpeed) {
        super(id, name, description, spritePath, ItemType.WEAPON, false);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
    }

    @Override
    public void Use() {

    }
}
