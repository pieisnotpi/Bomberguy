package com.pieisnotpi.bomberguy;

import com.pieisnotpi.bomberguy.animations.AnimSprite;
import com.pieisnotpi.bomberguy.animations.Animation;
import com.pieisnotpi.bomberguy.players.PlayerObject;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Texture;
import org.joml.Vector3f;

public class Bomb
{
    private static final Texture texture = Texture.getTextureFile("bomb.png");
    private final Animation animation = new Animation(null,
            new AnimSprite(texture, 0, 0, 32, 32, 1f),
            new AnimSprite(texture, 32, 0, 64, 32, 1f),
            new AnimSprite(texture, 64, 0, 96, 32, 1f));

    private float timeLeft = 3f;
    private int strength;
    private TexQuad quad;
    public boolean escaped = false;
    public PlayerObject owner;
    private Hitbox hitbox;
    private int tx, ty;

    public Bomb(int tx, int ty, float x, float y, float size, int strength, PlayerObject owner)
    {
        this.owner = owner;

        this.tx = tx;
        this.ty = ty;
        float px = size/32f;

        hitbox = new Hitbox(x + 9*px, y + 9*px, x + 23*px, y + 23*px, new Vector3f(0));

        this.strength = strength;
        animation.start();

        quad = new TexQuad(x, y, -0.1f, size, size, 0, animation.getCurSprite());
    }

    public Hitbox getHitbox()
    {
        return hitbox;
    }

    public boolean isExploded(float timeStep)
    {
        timeLeft -= timeStep;
        return timeLeft <= 0.0001f;
    }

    public boolean spriteUpdated(float timeStep)
    {
        if(animation.hasSpriteChanged(timeStep))
        {
            quad.setQuadSprite(animation.getCurSprite());
            return true;
        }
        return false;
    }

    public int getStrength()
    {
        return strength;
    }

    public int tx()
    {
        return tx;
    }

    public int ty()
    {
        return ty;
    }

    public TexQuad getQuad()
    {
        return quad;
    }
}
