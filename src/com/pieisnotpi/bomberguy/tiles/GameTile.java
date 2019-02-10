package com.pieisnotpi.bomberguy.tiles;

import com.pieisnotpi.engine.rendering.shaders.types.tex.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;

public abstract class GameTile
{
    private TexQuad quad;

    public GameTile(float x, float y, float size, Sprite sprite)
    {
        quad = new TexQuad(x, y, -0.1f, size, size, 0, sprite);
    }

    public TexQuad getQuad()
    {
        return quad;
    }
}
