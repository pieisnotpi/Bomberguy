package com.pieisnotpi.bomberguy;

import com.pieisnotpi.bomberguy.animations.AnimSprite;
import com.pieisnotpi.bomberguy.animations.Animation;
import com.pieisnotpi.bomberguy.physics.Hitbox;
import com.pieisnotpi.bomberguy.physics.DynamicPhysicsObject;
import com.pieisnotpi.bomberguy.players.PlayerObject;
import com.pieisnotpi.bomberguy.tiles.GameBoard;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Texture;

public class Bomb extends DynamicPhysicsObject
{
    private static final Texture texture = Texture.getTextureFile("bomb.png");
    private static final TexMaterial bombMaterial =
            new TexMaterial(Camera.ORTHO2D_R, texture);

    private final Animation animation = new Animation(null,
            new AnimSprite(texture, 0, 0, 32, 32, 1f),
            new AnimSprite(texture, 32, 0, 64, 32, 1f),
            new AnimSprite(texture, 64, 0, 96, 32, 1f));

    private float timeLeft = 3f;
    private int strength;
    private TexQuad quad;
    public PlayerObject owner;
    private int tx, ty;
    private GameBoard board;

    public Bomb(int tx, int ty, float x, float y, float size, int strength, PlayerObject owner, GameBoard board)
    {
        this.owner = owner;
        this.tx = tx;
        this.ty = ty;
        float px = size/32f;
        this.board = board;

        collidesWith = new int[]{1};
        bounciness = 0;
        collideable = false;

        transform.translate(x, y, 0);
        hitbox = new Hitbox(9*px, 9*px, 23*px, 23*px, transform.pos);

        this.strength = strength;
        animation.start();

        quad = new TexQuad(0, 0, -0.1f, size, size, 0, animation.getCurSprite());
        Mesh<TexQuad> mesh = new Mesh<>(bombMaterial, MeshConfig.QUAD);
        mesh.addPrimitive(quad);
        createRenderable(0, 0, mesh);
    }

    @Override
    public void update(float timeStep)
    {
        timeLeft -= timeStep;
        if(timeLeft <= 0.0001f)
        {
            board.detonateBomb(this);
            destroy();
        }
    }

    @Override
    public void drawUpdate(float timeStep)
    {
        super.drawUpdate(timeStep);

        if(animation.hasSpriteChanged(timeStep))
        {
            quad.setQuadSprite(animation.getCurSprite());
            renderable.getMeshes()[0].flagForBuild();
        }
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
}
