package com.pieisnotpi.editor.styles;

import com.pieisnotpi.editor.SpriteSet;
import com.pieisnotpi.editor.TileSprite;

public class MetalStyle extends TileStyle
{
    public MetalStyle()
    {
        super("metal.png");

        int tw = getTexture().image.width, th = getTexture().image.height;

        TileSprite[] metal_wall = new TileSprite[9];
        metal_wall[0] = new TileSprite(tw, th, 0, 0, 32, 32, 0);
        metal_wall[1] = new TileSprite(tw, th, 32, 0, 64, 32, 1);
        metal_wall[2] = new TileSprite(tw, th, 64, 0, 96, 32, 2);
        metal_wall[3] = new TileSprite(tw, th, 64, 32, 96, 64, 3);
        metal_wall[4] = new TileSprite(tw, th, 64, 64, 96, 96, 4);
        metal_wall[5] = new TileSprite(tw, th, 32, 64, 64, 96, 5);
        metal_wall[6] = new TileSprite(tw, th, 0, 64, 32, 96, 6);
        metal_wall[7] = new TileSprite(tw, th, 0, 32, 32, 64, 7);
        metal_wall[8] = new TileSprite(tw, th, 32, 32, 64, 64, 8);

        TileSprite[] metal_floor = new TileSprite[9];
        metal_floor[0] = new TileSprite(tw, th, 96, 0, 128, 32, 9);
        metal_floor[1] = new TileSprite(tw, th, 128, 0, 160, 32, 10);
        metal_floor[2] = new TileSprite(tw, th, 160, 0, 192, 32, 11);
        metal_floor[3] = new TileSprite(tw, th, 160, 32, 192, 64, 12);
        metal_floor[4] = new TileSprite(tw, th, 160, 64, 192, 96, 13);
        metal_floor[5] = new TileSprite(tw, th, 128, 64, 160, 96, 14);
        metal_floor[6] = new TileSprite(tw, th, 96, 64, 128, 96, 15);
        metal_floor[7] = new TileSprite(tw, th, 96, 32, 128, 64, 16);
        metal_floor[8] = new TileSprite(tw, th, 128, 32, 160, 64, 17);

        TileSprite[] metal_breakable = new TileSprite[1];
        metal_breakable[0] = new TileSprite(tw, th, 192, 0, 224, 32, 18);

        TileSprite[] metal_player = new TileSprite[1];
        metal_player[0] = new TileSprite(tw, th, 192, 32, 224, 64, 19);

        setTypes(new SpriteSet(metal_wall, 1), new SpriteSet(metal_floor, 0), new SpriteSet(metal_breakable, 2), new SpriteSet(metal_player, 3));
    }
}
