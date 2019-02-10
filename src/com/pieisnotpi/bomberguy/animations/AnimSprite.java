package com.pieisnotpi.bomberguy.animations;

import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;

public class AnimSprite extends Sprite
{
    private float time;

    public AnimSprite(Texture texture, int x0, int y0, int x1, int y1, float spriteTime)
    {
        super(texture, x0, y0, x1, y1);

        time = spriteTime;
    }

    public float getTime()
    {
        return time;
    }
}
