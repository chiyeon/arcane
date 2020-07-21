package com.kata.arcane.inventory.items;

import com.kata.arcane.Enums.*;

public class ItemDatabase {
    public static Item stick = new DefaultItem(0, "Stick", "A simple wooden stick", "items/stick.png", true);
    public static Item steelSword = new WeaponItem(1, "Steel Sword", "A sword cast in steel", "items/sword.png", 4, 1);
    public static Item steelDagger = new WeaponItem(2, "Steel Dagger", "A short dagger cast in steel", "items/dagger.png", 2, 0.5f);
    public static Item grass = new TileItem(3, "Grass", "A recessed hunk of grass", "tiles/grass.png", true, TileType.GRASS);
    public static Item dirt = new TileItem(4, "Dirt", "A whole hunk of dirt", "tiles/dirt.png", true, TileType.DIRT);
    public static Item dirtwall = new TileItem(5, "Dirt Wall", "A recessed hunk of dirt", "tiles/dirt_wall.png", true, TileType.DIRT_WALL);
    public static Item stone = new TileItem(6, "Stone", "Big piece of stone", "tiles/stone.png", true, TileType.STONE);
    public static Item pickaxe = new WeaponItem(7, "Steel Pickaxe", "Mostly pick, a little axe", "items/pick.png", 2, 1);
    public static Item wood = new TileItem(8, "Wood", "A tile of wood", "tiles/wood.png", true, TileType.WOOD);
    public static Item woodwall = new TileItem(9, "Wood Wall", "A wall of wood", "tiles/wood_wall.png", true, TileType.WOOD_WALL);
    public static Item light = new TileItem(10, "Light Block", "Emits light", "tiles/light.png", true, TileType.LIGHT);
}
