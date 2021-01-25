package com.pieisnotpi.editor.styles;

import com.pieisnotpi.editor.SpriteSet;
import com.pieisnotpi.editor.TileSprite;

public class StoneStyle extends TileStyle
{
    public StoneStyle()
    {
        super("stone.png");

        int tw = getTexture().image.width, th = getTexture().image.height;

        TileSprite[] stone_wall = new TileSprite[9];
        stone_wall[0] = new TileSprite(tw, th, 0, 0, 32, 32, 0);
        stone_wall[1] = new TileSprite(tw, th, 32, 0, 64, 32, 1);
        stone_wall[2] = new TileSprite(tw, th, 64, 0, 96, 32, 2);
        stone_wall[3] = new TileSprite(tw, th, 64, 32, 96, 64, 3);
        stone_wall[4] = new TileSprite(tw, th, 64, 64, 96, 96, 4);
        stone_wall[5] = new TileSprite(tw, th, 32, 64, 64, 96, 5);
        stone_wall[6] = new TileSprite(tw, th, 0, 64, 32, 96, 6);
        stone_wall[7] = new TileSprite(tw, th, 0, 32, 32, 64, 7);
        stone_wall[8] = new TileSprite(tw, th, 32, 32, 64, 64, 8);

        TileSprite[] grass_floor = new TileSprite[3];
        grass_floor[0] = new TileSprite(tw, th, 96, 0, 128, 32, 9);
        grass_floor[1] = new TileSprite(tw, th, 96, 32, 128, 64, 10);
        grass_floor[2] = new TileSprite(tw, th, 96, 64, 128, 96, 11);

        TileSprite[] stone_breakable = new TileSprite[1];
        stone_breakable[0] = new TileSprite(tw, th, 128, 0, 160, 32, 12);

        TileSprite[] stone_player = new TileSprite[1];
        stone_player[0] = new TileSprite(tw, th, 128, 32, 160, 64, 13);

        setTypes(new SpriteSet(stone_wall, 1), new SpriteSet(grass_floor, 0), new SpriteSet(stone_breakable, 2), new SpriteSet(stone_player, 3));
    }
}
