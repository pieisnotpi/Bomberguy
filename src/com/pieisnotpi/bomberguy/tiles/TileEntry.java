package com.pieisnotpi.bomberguy.tiles;

import com.pieisnotpi.engine.rendering.textures.Sprite;

public class TileEntry
{
    private int type;
    private Sprite sprite;

    public TileEntry(int tileType, Sprite sprite)
    {
        this.type = tileType;
        this.sprite = sprite;
    }

    public int getTileType()
    {
        return type;
    }

    public Sprite getSprite()
    {
        return sprite;
    }
}
