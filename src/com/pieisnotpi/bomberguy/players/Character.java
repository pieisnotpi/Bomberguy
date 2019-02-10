package com.pieisnotpi.bomberguy.players;

import com.pieisnotpi.bomberguy.animations.AnimSprite;
import com.pieisnotpi.bomberguy.animations.Animation;
import com.pieisnotpi.bomberguy.shaders.CharMaterial;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;

public class Character
{
    private static final Texture
            robotTexture = Texture.getTextureFile("robot.png"),
            carlTexture = Texture.getTextureFile("carl.png");

    public static final Character

            robot = new Character(robotTexture,
                new Animation(
                    new Sprite(robotTexture, 0, 0, 32, 32),
                    new AnimSprite(robotTexture, 32, 0, 64, 32, 0.2f),
                    new AnimSprite(robotTexture, 64, 0, 96, 32, 0.2f)),

                new Animation(
                    new Sprite(robotTexture, 0, 32, 32, 64),
                    new AnimSprite(robotTexture, 32, 32, 64, 64, 0.2f),
                    new AnimSprite(robotTexture, 64, 32, 96, 64, 0.2f))),

            carl = new Character(carlTexture,
                new Animation(
                    new Sprite(carlTexture, 0, 0, 32, 32),
                    new AnimSprite(carlTexture, 32, 0, 64, 32, 0.2f),
                    new AnimSprite(carlTexture, 64, 0, 96, 32, 0.2f)),

                new Animation(
                    new Sprite(carlTexture, 0, 32, 32, 64),
                    new AnimSprite(carlTexture, 32, 32, 64, 64, 0.2f),
                    new AnimSprite(carlTexture, 64, 32, 96, 64, 0.2f)));


    public final float hx0 = 7f, hy0 = 0f, hx1 = 24f, hy1 = 25f;
    private CharMaterial material;
    private Animation animUp, animDown;

    private Character(Texture texture, Animation animDown, Animation animUp)
    {
        this.animUp = animUp;
        this.animDown = animDown;
        material = new CharMaterial(Camera.ORTHO2D_R, texture);
    }

    public CharMaterial getMaterial()
    {
        return material;
    }

    public Animation getUpAnimation()
    {
        return animUp;
    }

    public Animation getDownAnimation()
    {
        return animDown;
    }
}
