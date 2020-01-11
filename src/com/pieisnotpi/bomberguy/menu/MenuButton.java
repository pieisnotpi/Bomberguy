package com.pieisnotpi.bomberguy.menu;

import com.pieisnotpi.bomberguy.font.PixelFont;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.ui.text.Text;
import org.joml.Vector3f;

public class MenuButton extends MenuItem
{
    private ButtonClickEvent buttonEvent;
    private Text text;

    public MenuButton(ButtonClickEvent event, String text, float textSize, MenuButton above, MenuButton below, MainMenu menu)
    {
        super(above, below, menu);

        this.text = new Text(PixelFont.getFont(), text, new Vector3f(0), Camera.ORTHO2D_R);

    }

    @Override
    public void onLeft()
    {

    }

    @Override
    public void onRight()
    {

    }

    @Override
    public void onClick()
    {

    }

    @Override
    public void highlight()
    {

    }

    @Override
    public void dehighlight()
    {

    }
}
