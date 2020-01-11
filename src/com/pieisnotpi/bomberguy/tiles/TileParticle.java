package com.pieisnotpi.bomberguy.tiles;

import com.pieisnotpi.bomberguy.physics.DynamicPhysicsObject;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.tex_c.TexCMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.tex_c.TexCQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.utility.Color;

public class TileParticle extends DynamicPhysicsObject
{
    private static final float
            LIFE_SPAN = 6f,
            LIFE_SPAN_VARIANCE = 2f,
            SLOWDOWN = 1.05f,
            MIN_SPEED = 300f;

    private float lifeSpan;
    private TexCQuad quad;
    private Color color;
    private GameBoard board;

    public TileParticle(float x, float y, float size, Texture texture, int texSize, int texX, int texY, float vx, float vy, Color color, GameBoard board)
    {
        super(x, y, 0, 0, size, -size);

        mass = 1f;
        bounciness = 0.5f;
        collidesWith = new int[]{0};

        this.color = color;
        this.board = board;

        lifeSpan = LIFE_SPAN + (float) (Math.random() - 0.5) * LIFE_SPAN_VARIANCE;

        quad = new TexCQuad(0, 0, -0.15f, size, -size, 0, new Sprite(texture, texX, texY, texX + texSize, texY + texSize, false), color);

        Mesh<TexCQuad> mesh = new Mesh<>(new TexCMaterial(Camera.ORTHO2D_R, texture), MeshConfig.QUAD);
        mesh.addPrimitive(quad);
        createRenderable(0, 0, mesh);

        velocity.set(vx, vy);
    }

    @Override
    public void drawUpdate(float timeStep)
    {
        if (lifeSpan <= 0)
        {
            destroy();
            board.getPhysics().removePhysicsObject(3, this);
        }
        else
        {
            quad.setColor(color);
            getRenderable().getMeshes()[0].flagForBuild();
            lifeSpan -= timeStep;

            velocity.mul(1f/SLOWDOWN, 1f/SLOWDOWN);
            if (velocity.lengthSquared() < MIN_SPEED) velocity.set(0);
        }
    }
}
