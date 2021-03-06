package com.kata.arcane.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.kata.arcane.Control;
import com.kata.arcane.Enums.*;
import com.kata.arcane.entities.Player;
import com.kata.arcane.inventory.items.ItemDatabase;
import com.kata.arcane.inventory.items.ItemInstance;
import com.kata.arcane.inventory.items.TileItem;
import com.kata.arcane.physics.Box2DFactory;
import com.kata.arcane.physics.Box2DWorld;

import java.util.ArrayList;
import java.util.HashMap;

public class Chunk {
    public static int WIDTH = 8;
    public static int HEIGHT = 64;

    public HashMap<Integer, Body> collisionBoxes;

    public Vector3 position;
    public Tile[] tiles;

    //new chunk, fills tiles with empty air and setups up collision boxes
    public Chunk(int x, int y) {
        position = new Vector3(x, y, 0);

        tiles = new Tile[WIDTH * HEIGHT];
        for(int i = 0; i < WIDTH; i++) {
            for(int j = 0; j < HEIGHT; j++) {
                tiles[i + j * WIDTH] = new Tile(TileType.AIR);
                tiles[i + j * WIDTH].rectangle.set(GetWorldPosition(i, j).x, GetWorldPosition(i, j).y, 8, 8);
            }
        }

        collisionBoxes = new HashMap<>();
    }

    //iterates through each chunk and calls the render function.
    public void RenderChunk(SpriteBatch batch) {
        for(int i = 0; i < WIDTH; i++) {
            for(int j = 0; j < HEIGHT; j++) {
                tiles[i + j * WIDTH].Render(batch, (int)position.x * WIDTH + i, (int)position.y * HEIGHT + j);
            }
        }
    }

    public void UpdateChunk(Box2DWorld world, Camera camera, Control control, Player player, GameWorld gameworld) {
        /*
        for(int i = 0; i < tiles.length; i++) {
            if(tiles[i].rectangle.contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))) {
                tiles[i].ChangeType(TileType.AIR);
                UpdateLightLevel();
                UpdateCollisionBoxes(world);
            }
        }*/

        //TEMP move to player class

        if(Gdx.input.isButtonPressed(0)) {
            Vector3 click = camera.unproject(new Vector3(control.mouseClickPosition.x, control.mouseClickPosition.y, 0));
            for(int i = 0; i < tiles.length; i++) {
                if(tiles[i].rectangle.contains(new Vector2(click.x, click.y))) {
                    ItemInstance item = player.inventory.items[player.inventory.selectedItem];
                    if(item != null && item.item instanceof TileItem && tiles[i].type == TileType.AIR) {
                        tiles[i].ChangeType(((TileItem)item.item).tileType);
                        player.inventory.RemoveItem(item.item, 1);
                    } else if(item != null && item.item == ItemDatabase.pickaxe && tiles[i].type != TileType.AIR) {
                        player.inventory.AddItem(tiles[i].dropItem, 1);
                        tiles[i].ChangeType(TileType.AIR);
                    }

                    RemoveCollisionBoxes(world.world);
                    UpdateLightLevel(gameworld);
                    UpdateCollisionBoxes(world);
                }
            }
        }
    }

    //easier way to get a tile from the x and y
    public Tile GetTile(int x, int y) {
        int index = x + y * WIDTH;
        if(index >= tiles.length || index < 0) {
            System.out.println("Attempted to retrieve a tile that doesn't exist!");
            return null;
        }

        return tiles[index];
    }

    //gets the tile above the specified coordinates if it exists
    public Tile GetTileAbove(int x, int y) {
        int index = x + (y + 1) * WIDTH;
        if(index >= tiles.length)
            return null;

        return tiles[index];
    }

    //checks if a tile exists, or if it is within the bounds of index
    public boolean DoesTileExist(int x, int y) {
        int index = x + y * WIDTH;
        if(index >= tiles.length || index < 0)
            return false;

        return true;
    }

    //gets all nearby tiles to teh coordinates, not very performant?
    public ArrayList<Tile> GetNearbyTiles(int x, int y) {
        ArrayList<Tile> nearby = new ArrayList<>();

        if(DoesTileExist(x, y + 1)) {
            nearby.add(GetTile(x, y + 1));
        }
        if(DoesTileExist(x, y - 1)) {
            nearby.add(GetTile(x, y - 1));
        }
        if(DoesTileExist(x + 1, y)) {
            nearby.add(GetTile(x + 1, y));
        }
        if(DoesTileExist(x - 1, y)) {
            nearby.add(GetTile(x - 1, y));
        }

        return nearby;
    }

    //the 'real world position' of a tile rather than its local chunk coordinates
    public Vector3 GetWorldPosition(int x, int y) {
        return new Vector3((position.x * WIDTH + x) * 8, y * 8, 0);
    }

    public void UpdateCollisionBoxes(Box2DWorld world) {
        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                if(GetTile(x, y).isSolid) {
                    ArrayList<Tile> nearbyTiles = GetNearbyTiles(x, y);
                    for(int i = 0; i < nearbyTiles.size(); i++) {
                        if(!nearbyTiles.get(i).isSolid) {
                            //set collision
                            collisionBoxes.put(x + y * WIDTH, Box2DFactory.createBody(world.world, 8, 8, GetWorldPosition(x, y), BodyDef.BodyType.StaticBody));
                            break;
                        }
                    }
                } else {
                    if(collisionBoxes.containsKey(x + y * WIDTH)) {
                        world.RemoveBody(collisionBoxes.get(x + y * WIDTH));
                        collisionBoxes.remove(x + y * WIDTH);
                    }
                }
            }
        }
    }

    public void RemoveCollisionBoxes(Box2DWorld world) {
        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                if(!GetTile(x, y).isSolid) {
                    if(collisionBoxes.containsKey(x + y * WIDTH)) {
                        world.RemoveBody(collisionBoxes.get(x + y * WIDTH));
                        collisionBoxes.remove(x + y * WIDTH);
                    }
                }
            }
        }
    }

    public void RemoveCollisionBox(World world, int x, int y) {
        if (collisionBoxes.containsKey(x + y * WIDTH)) {
            world.destroyBody(collisionBoxes.get(x + y));
            collisionBoxes.remove(x + y * WIDTH);
        }
    }

    public void RemoveCollisionBox(World world, int index) {
        if (collisionBoxes.containsKey(index)) {
            world.destroyBody(collisionBoxes.get(index));
            collisionBoxes.remove(index);
        }
    }

    public void UpdateLightLevel(GameWorld gameWorld) {
        //iterate through each tile x
        for(int x = 0; x < WIDTH; x++) {
            //get the highest tile
            int highestTileY = 0;
            for(int i = HEIGHT - 1; i >= 0; i--) {
                if(GetTile(x, i).isSolid) {
                    highestTileY = i;
                    break;
                }
            }

            float lightTravelDistance = 8;

            //light travels 10 UNITS atm
            if(highestTileY > 0) {
                float lowestShadedY = highestTileY - lightTravelDistance;
                for(int y = highestTileY; y >= 0; y--) {
                    if(y >= lowestShadedY) {
                        GetTile(x, y).lightLevel = ((float)y - lowestShadedY) / (highestTileY - lowestShadedY) * (3f/4f) + 0.25f;
                    } else {
                        GetTile(x, y).lightLevel = 0.25f;
                    }
                }
            }
        }

        Chunk nextChunk = gameWorld.GetChunk((int)position.x + 1);
        Chunk prevChunk = gameWorld.GetChunk((int)position.x - 1);

        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                Tile tile = GetTile(x, y);
                if(tile.type == TileType.LIGHT) {
                    int travelDistance = 8;
                    for(int i = x - travelDistance; i <= x + travelDistance; i++) {
                        for(int j = y - travelDistance; j <= y + travelDistance; j++) {
                            float distanceFromLightSource = (float) Math.sqrt(square(x - i) + square(y - j));
                            float lightLevel = travelDistance / (distanceFromLightSource * distanceFromLightSource);

                            //check if it is going into a new chunk
                            if(i > WIDTH) {
                                nextChunk.GetTile(i, j).ApplyLightLevel(lightLevel);
                            } else if(i < 0) {
                                prevChunk.GetTile(i, j).ApplyLightLevel(lightLevel);
                            } else {
                                GetTile(i, j).ApplyLightLevel(lightLevel);
                            }
                        }
                    }
                    tile.lightLevel = 1.0f;
                }
            }
        }
    }

    public float square(float c) {
        return c * c;
    }

    //completely destroys all collision boxes. used for unloading chunks
    public void RemoveCollisionBoxes(World world) {
        for(Body body : collisionBoxes.values()) {
            world.destroyBody(body);
        }
        collisionBoxes.clear();
    }
}
