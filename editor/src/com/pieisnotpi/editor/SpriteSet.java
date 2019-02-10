package com.pieisnotpi.editor;

public class SpriteSet
{
    public TileSprite[] sprites;
    public int current = 0, id;

    public SpriteSet(TileSprite[] sprites, int id)
    {
        this.sprites = sprites;
        this.id = id;
    }

    public void addToCurrent(float amount)
    {
        current += amount;

        if(current >= sprites.length) current = 0;
        else if(current < 0) current = sprites.length - 1;
    }

    public TileSprite getCurrent()
    {
        return sprites[current];
    }
}
