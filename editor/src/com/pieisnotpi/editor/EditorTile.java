package com.pieisnotpi.editor;

import com.pieisnotpi.engine.rendering.shaders.types.tex_c.TexCQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.ui.UiObject;
import com.pieisnotpi.engine.utility.Color;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class EditorTile extends UiObject
{
    private TexCQuad foreground, background;
    public TileSprite foreSprite, backSprite;
    private Vector3f pos;
    private EditorScene scene;
    private Color highlightColor = new Color(0.8f, 0.8f, 0.8f), blankColor = new Color(0, 0, 0, 0), backgroundColor = new Color(0, 0, 0, 0);
    public boolean foreShowing = false, backShowing = false, placed = false, destroyed = false;

    public EditorTile(float x, float y, float z, float scale, TileSprite sprite, EditorScene scene)
    {
        pos = new Vector3f(x, y, z);
        size.set(scale, scale, 0);
        this.foreSprite = sprite;
        this.backSprite = sprite;
        this.scene = scene;
        this.transform = null;

        foreground = new TexCQuad(x, y, z, scale, scale, 0, sprite, blankColor);
        foreground.enabled = false;
        background = new TexCQuad(x, y, z - 0.01f, scale, scale, 0, sprite, backgroundColor);
        background.enabled = false;

        scene.gameObjects.add(this);
    }

    public void onLeftHold()
    {
        if(mouseHoverStatus && !placed)
        {
            if(!foreground.enabled) registerFore();

            foreShowing = true;

            foreSprite = scene.getCurSprite();

            foreground.setQuadSprite(foreSprite);
            foreground.setColor(blankColor);

            placed = true;
        }
    }

    public void onLeftRelease()
    {
        placed = false;
    }

    public void onRightHold()
    {
        if(mouseHoverStatus && !placed)
        {
            if(!background.enabled) registerBack();

            backShowing = true;

            backSprite = scene.getCurSprite();

            background.setQuadSprite(backSprite);
            background.setColor(backgroundColor);

            placed = true;
        }
    }

    public void onRightRelease()
    {
        placed = false;
    }

    @Override
    public void onMiddleHold()
    {
        if(!mouseHoverStatus) destroyed = false;
        else if(!destroyed)
        {
            if(foreShowing)
            {
                unregisterFore();
                foreShowing = false;
                destroyed = true;
            }
            else if(backShowing)
            {
                unregisterBack();
                backShowing = false;
                destroyed = true;
            }

            onMouseEntered();
        }
    }

    public void onMiddleRelease()
    {
        destroyed = false;
    }

    public void onMouseEntered()
    {
        if(!foreShowing && !foreground.enabled) registerFore();

        foreground.setColor(highlightColor);
        foreground.setQuadSprite(scene.getCurSprite());
    }

    public void onMouseExited()
    {
        if(!foreShowing) unregisterFore();

        foreground.setQuadSprite(foreSprite);
        foreground.setColor(blankColor);
    }

    public void blank()
    {
        unregisterFore();
        unregisterBack();

        foreShowing = false;
        backShowing = false;
    }

    private void registerFore()
    {
        foreground.enabled = true;
        scene.mesh.addPrimitive(foreground);
    }

    private void unregisterFore()
    {
        foreground.enabled = false;
        scene.mesh.removePrimitive(foreground);
    }

    private void registerBack()
    {
        background.enabled = true;
        scene.mesh.addPrimitive(background);
    }

    private void unregisterBack()
    {
        background.enabled = false;
        scene.mesh.removePrimitive(background);
    }
    
    @Override
    public boolean isPointInsideObject(Vector2f point)
    {
        return (point.x >= pos.x && point.x <= pos.x + size.x && point.y >= pos.y && point.y <= pos.y + size.y);
    }
}
