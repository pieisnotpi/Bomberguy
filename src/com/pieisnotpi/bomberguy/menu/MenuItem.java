package com.pieisnotpi.bomberguy.menu;

import com.pieisnotpi.engine.ui.UiObject;

public abstract class MenuItem extends UiObject
{
    private MainMenu menu;
    private MenuItem above, below;

    public MenuItem(MenuItem above, MenuItem below, MainMenu menu)
    {
        this.above = above;
        this.below = below;
        this.menu = menu;
    }

    public MenuItem getAbove()
    {
        return above;
    }

    public MenuItem getBelow()
    {
        return below;
    }

    public MainMenu getMenu()
    {
        return menu;
    }

    public abstract void onLeft();

    public abstract void onRight();

    public abstract void onClick();

    public abstract void highlight();

    public abstract void dehighlight();
}
