package com.pieisnotpi.bomberguy.upgrades;

import com.pieisnotpi.bomberguy.players.PlayerObject;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import org.joml.Vector3f;

public class BombCountUpgrade extends Upgrade
{
    public static final int odds = 10;
    private static final Texture texture = Texture.getTextureFile("upgrades.png");
    private static final Sprite sprite = new Sprite(texture, 0, 0, 32, 32);

    public BombCountUpgrade(float size, Vector3f pos)
    {
        super(10, 10, 22, 22, pos, size, sprite);
    }

    @Override
    public boolean onCollect(PlayerObject player)
    {
        if(player.getBombCount() < PlayerObject.MAX_BOMBS)
        {
            player.upgradeCount();
            return true;
        }
        return false;
    }
}
