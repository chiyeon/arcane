package com.kata.arcane.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.kata.arcane.Enums.*;
import com.kata.arcane.inventory.items.*;
import com.kata.arcane.physics.Box2DFactory;

public class Player extends Entity {

    public int health;
    public float movementSpeed = 49;
    public Inventory inventory;
    public ArmorItem[] equippedArmor;
    //public WeaponItem equippedWeapon;
    public boolean left, right, up, down;
    public Body body;
    public float dirX;

    public Player(Vector3 position, World world) {
        super(position, new Texture("player.png"), 8, 16);
        inventory = new Inventory(10);
        equippedArmor = new ArmorItem[3];
        body = Box2DFactory.createBody(world, 7, 15, position, BodyDef.BodyType.DynamicBody);

        inventory.AddItem(ItemDatabase.pickaxe, 1);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        body.setLinearVelocity(dirX * movementSpeed, body.getLinearVelocity().y);
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;

        inventory.update();

        if(Gdx.input.isKeyJustPressed(Input.Keys.Q) && inventory.showInventory && inventory.items[inventory.selectedItem] != null) {
            Item item = inventory.items[inventory.selectedItem].item;
            //if(item instanceof WeaponItem) {
            //    EquipWeapon(inventory.selectedItem);
            //} else
            if(item instanceof ArmorItem) {
                EquipArmor(inventory.selectedItem);
            }
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            inventory.showInventory = !inventory.showInventory;
        }


        else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            inventory.AddItem(ItemDatabase.light, 1);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            inventory.AddItem(ItemDatabase.steelSword, 1);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            inventory.AddItem(ItemDatabase.steelDagger, 1);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            inventory.AddItem(ItemDatabase.grass, 1);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            inventory.AddItem(ItemDatabase.dirt, 1);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            inventory.AddItem(ItemDatabase.dirtwall, 1);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
            inventory.AddItem(ItemDatabase.stone, 1);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
            inventory.AddItem(ItemDatabase.wood, 1);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
            inventory.AddItem(ItemDatabase.woodwall, 1);
        }
    }

    public void EquipArmor(int index) {
        ArmorItem item = (ArmorItem)inventory.items[index].item;
        int slot = item.armorType.ordinal();

        inventory.items[index] = null;

        if(equippedArmor[slot] != null) {
            //unequip armor first
            UnEquipArmor(item.armorType);
        }

        equippedArmor[slot] = item;
        System.out.println("Equipped a " + item.itemName + " in the " + item.armorType + " slot.");
        //TODO apply stats
    }

    /*
    public void EquipWeapon(int index) {
        WeaponItem item = (WeaponItem)inventory.items[index].item;

        inventory.items[index] = null;


        if(equippedWeapon != null) {
            UnEquipWeapon();
        }

        equippedWeapon = item;
        System.out.println("Equipped a " + item.itemName + " weapon.");
        //TODO apply stats n stuff
    }*/

    public void UnEquipArmor(ArmorType type) {
        int slot = type.ordinal();
        if(inventory.AddItem(equippedArmor[slot], 1)) {
            equippedArmor[slot] = null;
            System.out.println("Unequipped armor in the " + type + " slot.");
            //TODO undo stats
        } else {
            System.out.println("Tried to unequip armor in slot " + type + " but inventory is full!");
        }
    }

    /*
    public void UnEquipWeapon() {
        if(inventory.AddItem(equippedWeapon, 1)) {
            equippedWeapon = null;
            System.out.println("Unequipped weapon.");
            //TODO undo stats
        } else {
            System.out.println("Tried to unequip weapon but inventory is full!");
        }
    }*/
}
