package com.pieisnotpi.bomberguy.maps;

import com.pieisnotpi.engine.utility.FileUtility;

public class GameMaps
{
    public static final GameMap
            metal1 = new GameMap(FileUtility.findFile("/assets/levels/metal.lvl"), "Metal 1"),
            metal2 = new GameMap(FileUtility.findFile("/assets/levels/metal_custom.lvl"), "Metal 2"),
            metal3 = new GameMap(FileUtility.findFile("/assets/levels/metal_custom_2.lvl"), "Metal 3"),
            metal4 = new GameMap(FileUtility.findFile("/assets/levels/metal_custom_3.lvl"), "Metal 4"),
            metal5 = new GameMap(FileUtility.findFile("/assets/levels/metal_custom_4.lvl"), "Metal 5"),
            metal6 = new GameMap(FileUtility.findFile("/assets/levels/metal_custom_5.lvl"), "Metal 6"),
            stone1 = new GameMap(FileUtility.findFile("/assets/levels/stone_custom.lvl"), "Stone 1");
}
