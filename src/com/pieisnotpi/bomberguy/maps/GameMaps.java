package com.pieisnotpi.bomberguy.maps;

import com.pieisnotpi.engine.utility.FileUtility;

public class GameMaps
{
    public static final GameMap
            metal1 = new GameMap(FileUtility.findFile("/assets/levels/metal.lvl")),
            metal2 = new GameMap(FileUtility.findFile("/assets/levels/metal_custom.lvl")),
            metal3 = new GameMap(FileUtility.findFile("/assets/levels/metal_custom_2.lvl")),
            metal4 = new GameMap(FileUtility.findFile("/assets/levels/metal_custom_3.lvl")),
            metal5 = new GameMap(FileUtility.findFile("/assets/levels/metal_custom_4.lvl")),
            metal6 = new GameMap(FileUtility.findFile("/assets/levels/metal_custom_5.lvl"));
}
