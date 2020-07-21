package com.kata.arcane.tiles;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kata.arcane.Control;
import com.kata.arcane.Enums.*;
import com.kata.arcane.entities.Player;
import com.kata.arcane.lib.FastNoise;
import com.kata.arcane.physics.Box2DWorld;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.*;

public class GameWorld {
    //list of ALL the chunks in the world. Generating each chunk as the player encountered it and leaving it in the cache seemed
    //not performant at all. Better solution?
    public HashMap<Integer, Chunk> chunks;
    //the amount of chunks to immediately render, based on render distance
    //public HashMap<Integer, Chunk> chunksToRender;
    public ArrayList<Chunk> visibleChunks;
    //noise map of the entire world, generated on world creation
    private float[] perlinNoise;
    //this is the width of the world in CHUNKS, total. This number is divided by two to ensure x = 0 is the middle of the map. Stick to even numbers here.
    private int worldWidth = 50; //in chunks
    //this is the SEED of the world. All random elements are seeded by this and this is used in the perlin noise calculation.
    //Can be found using the time of date to the second.
    private int seed;
    //the collision world
    private Box2DWorld boxWorld;
    //number of chunks draw in front and behind the player.
    private int renderDistance = 4;

    private Random random;

    public GameWorld(Box2DWorld boxWorld) {
        //first get random seed based on current time
        Calendar calendar = Calendar.getInstance();
        seed = calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND);

        //seed the random and create box world, as well as generate the noise by multiplying two perlin noise layers together
        random = new Random(seed);
        this.boxWorld = boxWorld;

        FastNoise fastNoise = new FastNoise();

        float[] noise1 = new float[worldWidth * Chunk.WIDTH];
        for(int i = 0; i < noise1.length; i++) {
            noise1[i] = fastNoise.GetPerlin(i + seed, 0);
        }

        float[] noise2 = new float[worldWidth * Chunk.WIDTH];
        for(int i = 0; i < noise2.length; i++) {
            noise2[i] = fastNoise.GetPerlin((i + seed) * 10, 0);
        }

        perlinNoise = CombineNoises(noise1, noise2);

        chunks = new HashMap<>();
        visibleChunks = new ArrayList<Chunk>();

        //this generates EVERY CHUNK in the entire world (which is LIMITED, not infinite.)
        //by storing it to memory, can efficiently load and unload chunks as they are needed rather than generating new ones
        for(int i = -worldWidth/2; i < worldWidth/2; i++) {
            GenerateChunk(i);
        }

    }

    //generates the chunk
    public void GenerateChunk(int position) {
        if(position >= worldWidth / 2 || position < -worldWidth / 2) {
            return;
        }
        Chunk chunk = new Chunk(position, 0);
        //i is the local position of the tile (within the chunk)
        //first iterates through each x value in the chunk
        for(int i = 0; i < Chunk.WIDTH; i++) {
            //calculate the real world position y for each x
            int y = 25 + (int)(10 * perlinNoise[(position + worldWidth/2) * Chunk.WIDTH + i]);
            //the current local y (within the chunk) and is the same as global y
            for(int j = 0; j <= y; j++) {
                if(j == y) {
                    chunk.tiles[i + j * Chunk.WIDTH].ChangeType(TileType.GRASS);

                    if(random.nextFloat() < 0.1f) {
                        //we must add one because this is the position of the grass block
                        CreateTree(chunk, i, j + 1);
                    }
                } else if(j <= 20) {
                    chunk.tiles[i + j * Chunk.WIDTH].ChangeType(TileType.STONE);
                } else {
                    chunk.tiles[i + j * Chunk.WIDTH].ChangeType(TileType.DIRT);
                }
            }
        }
        chunk.UpdateLightLevel(this);
        chunks.put(position, chunk);
    }

    public void UpdateWorld(int playerX, Camera camera, Control control, Player player) {

        //this must be scaled down to find which chunk the player is in.
        int position = playerX / Chunk.WIDTH / 8;

        //iterate through the visible chunks. if one is NOT within render distance, remove it so it is not rendered.
        for(int i = 0; i < visibleChunks.size(); i++) {
            //finally update the chunks
            visibleChunks.get(i).UpdateChunk(boxWorld, camera, control, player, this);

            float chunkX = visibleChunks.get(i).position.x;
            /*
            if the chunk position is LESS Than the player position - render distance
            or
            if the chunk position is GREATER THAN OR EQUAL TO the player position + render distance
            this is because when adding chunks, iterates through the render distance where it is EQUAL to negative distance
            and stops before reaching equal to render distance
             */
            if(chunkX < position - renderDistance || chunkX >= position + renderDistance) {
                visibleChunks.get(i).RemoveCollisionBoxes(boxWorld.world);
                visibleChunks.remove(i);
            }
        }

        for(int i = -renderDistance; i < renderDistance; i++) {
            Chunk chunk = GetChunk(position + i);

            if(!visibleChunks.contains(chunk)) {
                chunk.UpdateCollisionBoxes(boxWorld);
                visibleChunks.add(chunk);
            }
        }

        /*OLD SYSTEM OF ADDING/REMOVING CHUNKS USING A HASHMAP. INEFFICIENT.


        //this must be scaled down to find which chunk the player is in.
        int position = playerX / Chunk.WIDTH / 8;

        //iterate through all the chunks that should exist, if a chunk enters then add it.
        //this covers all chunks behind and in front of the player
        for(int i = -renderDistance; i < renderDistance; i++) {
            Chunk chunk = GetChunk(position + i);
            //if the chunk to render is not in the map
            if(!chunksToRender.containsValue(chunk)) {

                //if another chunk exist in place of it, remove the extra chunk
                if(chunksToRender.containsKey(i)) {
                    chunksToRender.get(i).RemoveCollisionBoxes(boxWorld.world);
                    chunksToRender.remove(i);
                }

                //then pass in the new chunk to render
                chunk.UpdateCollisionBoxes(boxWorld.world);
                chunksToRender.put(i, chunk);
            }


            if(chunksToRender.get(i) != chunk) {
                chunksToRender.get(i).RemoveCollisionBoxes(boxWorld.world);
                chunksToRender.remove(i);
            }
        }*/
    }

    public void RenderWorld(SpriteBatch batch) {

        for(Chunk chunk : visibleChunks) {
            chunk.RenderChunk(batch);
        }
    }

    public Chunk GetChunk(int position) {
        if(!chunks.containsKey(position)) {
            return null;
            //return null because chunks are now pre-generated
            //GenerateChunk(position);
        }

        return chunks.get(position);
    }

    //Creates a tree at a given position with a random height (max 8 min 3)
    public void CreateTree(Chunk chunk, int x, int y) {
        int treeHeight = 2+random.nextInt(5);

        chunk.GetTile(x, y).ChangeType(TileType.TREE_BASE);
        for(int i = 1; i < treeHeight; i++) {
            chunk.GetTile(x, y + i).ChangeType(TileType.TREE_MIDDLE);
        }
        chunk.GetTile(x, y + treeHeight).ChangeType(TileType.TREE_CAP);
    }

    public float[] CombineNoises(float[] noise1, float[] noise2) {
        float[] finalNoise = new float[noise1.length];
        for(int i = 0; i < noise1.length; i++) {
            finalNoise[i] = (noise1[i] + noise2[i]) / 2f;
        }
        return finalNoise;
    }

    public void UpdateVisibleChunkCollisionBoxes() {
        for(Chunk chunk : visibleChunks) {
            chunk.UpdateCollisionBoxes(boxWorld);
        }
    }
}
