package com.pieisnotpi.bomberguy.menu;

import com.pieisnotpi.bomberguy.font.PixelFont;
import com.pieisnotpi.bomberguy.maps.GameMap;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.ui.text.Text;
import org.joml.Vector3f;

public class LevelSelect extends MenuItem
{
    private int current = 0;

    private GameMap[] maps;
    private Text levelText;

    public LevelSelect(MenuItem above, MenuItem below, MainMenu menu, GameMap... maps)
    {
        super(above, below, menu);

        this.maps = maps;

        String title = maps[0].getTitle();

        levelText = new Text(PixelFont.getFont(), title, new Vector3f(), Camera.ORTHO2D_R);
        levelText.getTransform().setScaleCentered(4);
        addChild(levelText);
    }

    @Override
    public void onLeft()
    {
        current--;
        if(current < 0) current = maps.length - 1;
        getMenu().setNextLevel(maps[current]);
    }

    @Override
    public void onRight()
    {
        current++;
        if(current >= maps.length) current = 0;
        getMenu().setNextLevel(maps[current]);
    }

    @Override
    public void onClick()
    {
        // EMPTY
    }
}
