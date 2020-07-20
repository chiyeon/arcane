package com.kata.arcane.inventory.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    public ItemInstance[] items;
    public int selectedItem = 0;
    public boolean showInventory = false;

    public Inventory(int maxItems) {
        items = new ItemInstance[maxItems];
    }

    public void update() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            selectedItem--;
            if(selectedItem < 0) selectedItem = items.length - 1;
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            selectedItem++;
            if(selectedItem >= items.length - 1) selectedItem = 0;
        }
    }

    public boolean AddItem(Item item, int quantity) {
        for(int i = 0; i < items.length; i++) {
            if(items[i] != null) {
                if(items[i].item == item && item.itemStackable) {
                    items[i].quantity += quantity;
                    return true;
                }
            }
        }

        for(int i = 0; i < items.length; i++) {
            if(items[i] == null) {
                items[i] = new ItemInstance(item, quantity);
                return true;
            }
        }

        System.out.println("Inventory is full!");
        return false;
    }

    public boolean RemoveItem(Item item, int quantity) {
        for(int i = 0; i < items.length; i++) {
            if(items[i] != null && items[i].item == item) {
                items[i].quantity -= quantity;
                if(items[i].quantity <= 0) {
                    items[i] = null;
                }
                return true;
            }
        }
        return false;
    }

    public void RenderSelectItem(BitmapFont font, SpriteBatch batch, int xPos, int yPos) {
        if(items[selectedItem] == null)
            return;

        Item item = items[selectedItem].item;

        String message = item.itemName;
        message += items[selectedItem].quantity > 1 ? " (x" + items[selectedItem].quantity + ")" : "";

        message += "\n" + item.itemType + " ITEM\n\n" + item.itemDescription;

        batch.draw(item.itemSprite, xPos, yPos - 32, 32, 32);
        font.draw(batch, message, xPos, yPos - 32 - 20);
    }

    public void RenderInventory(BitmapFont font, SpriteBatch batch, int xPos, int yPos) {
        String message = "Inventory:\n";
        for(int i = 0; i < items.length; i++) {
            message += i == selectedItem ? "> " : "  ";

            if(items[i] != null) {
                message += items[i].item.itemName;

                if(items[i].quantity > 1) {
                    message += " (x" + items[i].quantity + ")\n";
                } else {
                    message += "\n";
                }
            } else {
                message += "empty\n";
            }
        }

        font.draw(batch, message, xPos, yPos);
    }
}
