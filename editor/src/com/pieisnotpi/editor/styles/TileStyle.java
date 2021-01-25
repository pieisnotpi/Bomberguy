package com.pieisnotpi.editor.styles;

import com.pieisnotpi.editor.SpriteSet;
import com.pieisnotpi.engine.rendering.textures.Texture;

public class TileStyle
{
    private Texture texture;
    private SpriteSet[] types;
    private String texName;
    private int curType;

    public TileStyle(String texName)
    {
        this.texName = texName;
        this.texture = Texture.getTextureFile(texName);
        curType = 0;
    }

    protected void setTypes(SpriteSet... spriteSets)
    {
        types = spriteSets;
    }

    public void iterateSet(int amount)
    {
        curType += amount;

        if(curType >= types.length) curType = 0;
        else if(curType < 0) curType = types.length - 1;
    }

    public SpriteSet[] getTypes()
    {
        return types;
    }

    public SpriteSet getCurSet()
    {
        return types[curType];
    }

    public Texture getTexture()
    {
        return texture;
    }

    public String getTexName()
    {
        return texName;
    }
}
