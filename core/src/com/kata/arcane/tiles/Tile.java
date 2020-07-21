package com.kata.arcane.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.kata.arcane.Enums.*;
import com.kata.arcane.inventory.items.Item;
import com.kata.arcane.inventory.items.ItemDatabase;

public class Tile {
    public static int WIDTH = 8;

    public Texture texture;
    public TileType type;
    public boolean isSolid;
    public float lightLevel;
    public Item dropItem;

    //temp
    Rectangle rectangle;

    public Tile(TileType type) {
        ChangeType(type);
        lightLevel = 0f;
        rectangle = new Rectangle(0, 0, 8, 8);
        this.dropItem = null;
    }

    private void UpdateTexture() {
        isSolid = true;
        switch(this.type) {
            case AIR:
                isSolid = false;
                break;
            case GRASS:
                texture = new Texture("tiles/grass.png");
                dropItem = ItemDatabase.grass;
                break;
            case DIRT:
                texture = new Texture("tiles/dirt.png");
                dropItem = ItemDatabase.dirt;
                break;
            case STONE:
                texture = new Texture("tiles/stone.png");
                dropItem = ItemDatabase.stone;
                break;
            case TREE_CAP:
                lightLevel = 1.0f;
                texture = new Texture("tiles/tree_cap.png");
                dropItem = ItemDatabase.wood;
                isSolid = false;
                break;
            case TREE_MIDDLE:
                lightLevel = 1.0f;
                texture = new Texture("tiles/tree_middle.png");
                dropItem = ItemDatabase.wood;
                isSolid = false;
                break;
            case TREE_BASE:
                lightLevel = 1.0f;
                texture = new Texture("tiles/tree_base.png");
                dropItem = ItemDatabase.wood;
                isSolid = false;
                break;
            case DIRT_WALL:
                texture = new Texture("tiles/dirt_wall.png");
                dropItem = ItemDatabase.dirtwall;
                isSolid = false;
                break;
            case WOOD:
                texture = new Texture("tiles/wood.png");
                dropItem = ItemDatabase.wood;
                break;
            case WOOD_WALL:
                texture = new Texture("tiles/wood_wall.png");
                dropItem = ItemDatabase.woodwall;
                isSolid = false;
                break;
            case LIGHT:
                texture = new Texture("tiles/light.png");
                dropItem = ItemDatabase.light;
                isSolid = false;
                break;
        }
    }

    public void Render(SpriteBatch batch, int x, int y) {
        if(type != TileType.AIR) {
            batch.setColor(lightLevel, lightLevel, lightLevel, 1);
            batch.draw(texture, x * WIDTH, y * WIDTH);
        }
    }

    public void ChangeType(TileType type) {
        this.type = type;
        UpdateTexture();
    }

    public void ApplyLightLevel(float newLevel) {
        if(lightLevel < newLevel) {
            lightLevel = newLevel;
        }
    }
}
