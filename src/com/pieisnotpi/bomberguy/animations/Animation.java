package com.pieisnotpi.bomberguy.animations;

import com.pieisnotpi.engine.rendering.textures.Sprite;

public class Animation
{
    private Sprite idle;
    private AnimSprite[] anim;
    private boolean running = false, changeOverride = false;
    private int curSprite;
    private float timeTilChange;

    public Animation(Sprite idleSprite, AnimSprite... animSprites)
    {
        idle = idleSprite;
        anim = animSprites;
    }

    public void start()
    {
        running = true;
        changeOverride = true;
        curSprite = 0;
        timeTilChange = anim[0].getTime();
    }

    public void end()
    {
        running = false;
        changeOverride = true;
    }

    public boolean isRunning()
    {
        return running;
    }

    public boolean hasSpriteChanged(float step)
    {
        if(changeOverride)
        {
            changeOverride = false;
            return true;
        }

        if(!running) return false;

        timeTilChange -= step;

        if(timeTilChange <= 0)
        {
            incrementSprite();
            return true;
        }

        return false;
    }

    public Sprite getCurSprite()
    {
        if(running) return anim[curSprite];
        else return idle;
    }

    private void incrementSprite()
    {
        curSprite++;

        if(curSprite >= anim.length) curSprite = 0;

        timeTilChange = anim[curSprite].getTime();
    }
}
