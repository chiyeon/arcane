package com.kata.arcane.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.kata.arcane.Enums.*;

public class Tile {
    public static int WIDTH = 8;

    public Texture texture;
    public TileType type;
    public boolean isSolid;
    public float lightLevel;

    //temp
    Rectangle rectangle;

    public Tile(TileType type) {
        ChangeType(type);
        lightLevel = 1.0f;
        rectangle = new Rectangle(0, 0, 8, 8);
    }

    private void UpdateTexture() {
        isSolid = true;
        switch(this.type) {
            case AIR:
                isSolid = false;
                break;
            case GRASS:
                texture = new Texture("tiles/grass.png");
                break;
            case DIRT:
                texture = new Texture("tiles/dirt.png");
                break;
            case STONE:
                texture = new Texture("tiles/stone.png");
                break;
            case TREE_CAP:
                texture = new Texture("tiles/tree_cap.png");
                isSolid = false;
                break;
            case TREE_MIDDLE:
                texture = new Texture("tiles/tree_middle.png");
                isSolid = false;
                break;
            case TREE_BASE:
                texture = new Texture("tiles/tree_base.png");
                isSolid = false;
                break;
            case DIRT_WALL:
                texture = new Texture("tiles/dirt_wall.png");
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
}
