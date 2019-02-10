package com.pieisnotpi.editor;

import com.pieisnotpi.engine.rendering.textures.Sprite;

public class TileSprite extends Sprite
{
    public int entry;

    public TileSprite(int texWidth, int texHeight, int x0, int y0, int x1, int y1, int entry)
    {
        super(texWidth, texHeight, x0, y0, x1, y1);
        this.entry = entry;
    }
}
