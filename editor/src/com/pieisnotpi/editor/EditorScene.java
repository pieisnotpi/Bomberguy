package com.pieisnotpi.editor;

import com.pieisnotpi.editor.styles.MetalStyle;
import com.pieisnotpi.editor.styles.StoneStyle;
import com.pieisnotpi.editor.styles.TileStyle;
import com.pieisnotpi.engine.input.keyboard.Keyboard;
import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.mesh.Transform;
import com.pieisnotpi.engine.rendering.shaders.types.tex_c.TexCMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.tex_c.TexCQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EditorScene extends Scene
{
    private static final int w = 16, h = 16;
    private EditorTile[][] tiles = new EditorTile[w][h];
    private TileStyle style;

    public Mesh<TexCQuad> mesh;
    public GameObject tilesObject;

    public EditorScene init() throws Exception
    {
        super.init();

        name = "Editor";

        //style = new MetalStyle();
        style = new StoneStyle();
        mesh = new Mesh<>(new TexCMaterial(Camera.ORTHO2D_S, style.getTexture()), MeshConfig.QUAD);
        addGameObject(tilesObject = new GameObject(new Renderable(1, 0, new Transform(), mesh)));

        addCamera(new Camera(90, new Vector2f(0, 0), new Vector2f(1, 1)));
        clearColor.set(0.6f, 0.6f, 0.6f);

        int max = Integer.max(tiles.length, tiles[0].length);

        float xScale = 2f/max, yScale = 2f/max, min = Float.min(xScale, yScale), xOffset = -tiles.length*xScale/2, yOffset = -tiles[0].length*yScale/2;
        TileSprite current = style.getCurSet().getCurrent();

        for(int x = 0; x < tiles.length; x++) for(int y = 0; y < tiles[0].length; y++) tiles[x][y] = new EditorTile(xOffset + x*xScale, yOffset + y*yScale, -0.5f, min, current, this);

        return this;
    }

    @Override
    public void drawUpdate(float timeStep) throws Exception
    {
        super.drawUpdate(timeStep);
        mesh.flagForBuild();
    }

    public TileSprite getCurSprite()
    {
        return style.getCurSet().getCurrent();
    }

    public void onKeyPressed(int key, int mods)
    {
        super.onKeyPressed(key, mods);

        if(key == Keyboard.KEY_LEFT_BRACKET) onScroll(-1, 0);
        else if(key == Keyboard.KEY_RIGHT_BRACKET) onScroll(1, 0);
        else if(key == Keyboard.KEY_S) save("stone_custom.lvl");
    }

    public void onScroll(float x, float y)
    {
        super.onScroll(x, y);

        if(y < 0) style.getCurSet().addToCurrent(-1);
        else if(y > 0) style.getCurSet().addToCurrent(1);

        if(x < 0) style.iterateSet(-1);
        else if(x > 0) style.iterateSet(1);

        gameObjects.forEach(gameObject ->
        {
            if(!gameObject.getClass().equals(EditorTile.class)) return;

            EditorTile tile = (EditorTile) gameObject;
            if(tile.isMouseOver()) tile.onMouseEntered();
        });
    }

    private void save(String fileName)
    {
        try(PrintWriter writer = new PrintWriter(fileName))
        {
            writer.println(style.getTexName());
            writer.println("TILES");
            for (SpriteSet t : style.getTypes())
            {
                for(TileSprite sprite : t.sprites)
                {
                    writer.println();
                    writer.println(sprite.entry);
                    writer.println(t.id);
                    writer.printf("%d %d %d %d%n", sprite.x0, sprite.y0, sprite.x1, sprite.y1);
                }
            }
            writer.println("END");
            writer.printf("%d %d%n", w, h);

            for(int y = h - 1; y >= 0; y--)
            {
                for(int x = 0; x < w; x++)
                {
                    EditorTile tile = tiles[x][y];
                    int value = tile.backShowing ? tile.backSprite.entry : -1;
                    writer.printf("%d ", value);
                }
                writer.println();
            }

            writer.println();

            for(int y = h - 1; y >= 0; y--)
            {
                for(int x = 0; x < w; x++)
                {
                    EditorTile tile = tiles[x][y];
                    int value = tile.foreShowing ? tile.foreSprite.entry : -1;
                    writer.printf("%d ", value);
                }
                writer.println();
            }
        }
        catch (IOException e) {}
    }
}
