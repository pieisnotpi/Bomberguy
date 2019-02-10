package com.pieisnotpi.bomberguy.upgrades;

import com.pieisnotpi.bomberguy.Hitbox;
import com.pieisnotpi.bomberguy.players.PlayerObject;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import org.joml.Vector3f;

public abstract class Upgrade
{
    private Hitbox hitbox;
    private TexQuad quad;

    public Upgrade(int hx0, int hy0, int hx1, int hy1, Vector3f pos, float size, Sprite sprite)
    {
        hitbox = new Hitbox(hx0, hy0, hx1, hy1, pos);
        quad = new TexQuad(pos.x, pos.y, -0.15f, size, size, 0, sprite);
    }

    public Hitbox getHitbox()
    {
        return hitbox;
    }

    public TexQuad getQuad()
    {
        return quad;
    }

    public abstract boolean onCollect(PlayerObject player);
}
