package com.pieisnotpi.bomberguy.upgrades;

import com.pieisnotpi.bomberguy.players.PlayerObject;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import org.joml.Vector3f;

public class StrengthUpgrade extends Upgrade
{
    public static final int odds = 10 + BombCountUpgrade.odds;

    private static final Texture texture = Texture.getTextureFile("upgrades.png");
    private static final Sprite sprite = new Sprite(texture, 32, 0, 64, 32);

    public StrengthUpgrade(float size, Vector3f pos)
    {
        super(10, 10, 22, 22, pos, size, sprite);
    }

    @Override
    public boolean onCollect(PlayerObject player)
    {
        if(player.getBombStrength() < PlayerObject.MAX_STRENGTH)
        {
            player.upgradeStrength();
            return true;
        }
        return false;
    }
}
