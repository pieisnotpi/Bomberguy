package com.pieisnotpi.bomberguy.upgrades;

import com.pieisnotpi.bomberguy.physics.DynamicPhysicsObject;
import com.pieisnotpi.bomberguy.physics.Hitbox;
import com.pieisnotpi.bomberguy.players.PlayerObject;
import com.pieisnotpi.bomberguy.shaders.CharMaterial;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import org.joml.Vector3f;

public abstract class Upgrade extends DynamicPhysicsObject
{
    private TexQuad quad;

    private static final float
            SLOWDOWN = 1.1f,
            MIN_SPEED = 300f;

    public Upgrade(int hx0, int hy0, int hx1, int hy1, Vector3f pos, float size, Sprite sprite)
    {
        super(pos.x, pos.y, hx0, hy0, hx1, hy1);

        mass = 4f;
        bounciness = 0.25f;
        collidesWith = new int[]{0, 4};

        quad = new TexQuad(0, 0, -0.11f, size, size, 0, sprite);
        Mesh<TexQuad> mesh = new Mesh<>(new CharMaterial(Camera.ORTHO2D_R, Texture.getTextureFile("upgrades.png")), MeshConfig.QUAD);
        mesh.addPrimitive(quad);
        createRenderable(0, 0, mesh);
    }

    public Hitbox getHitbox()
    {
        return hitbox;
    }

    public TexQuad getQuad()
    {
        return quad;
    }

    @Override
    public void drawUpdate(float timeStep)
    {
        velocity.mul(1f/SLOWDOWN, 1f/SLOWDOWN);
        if (velocity.lengthSquared() < MIN_SPEED) velocity.set(0);
    }

    public abstract boolean onCollect(PlayerObject player);
}
