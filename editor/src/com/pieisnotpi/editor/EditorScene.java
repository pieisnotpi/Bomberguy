package com.pieisnotpi.editor;

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
    private static final int w = 24, h = 16;
    private SpriteSet curSpriteSet;
    private List<SpriteSet> types = new ArrayList<>();
    private EditorTile[][] tiles = new EditorTile[w][h];
    public Mesh<TexCQuad> mesh;

    private int curType = 0;
    
    public GameObject tilesObject;

    public EditorScene init() throws Exception
    {
        super.init();

        name = "Editor";

        Texture tex = Texture.getTextureFile("metal.png");
        assert tex != null;
        int tw = tex.image.width, th = tex.image.height;

        mesh = new Mesh<>(new TexCMaterial(Camera.ORTHO2D_S, tex), MeshConfig.QUAD);
        addGameObject(tilesObject = new GameObject(new Renderable(1, 0, new Transform(), mesh)));

        addCamera(new Camera(90, new Vector2f(0, 0), new Vector2f(1, 1)));
        clearColor.set(0.6f, 0.6f, 0.6f);

        TileSprite[] metal_wall = new TileSprite[9];
        metal_wall[0] = new TileSprite(tw, th, 0, 0, 32, 32, 0);
        metal_wall[1] = new TileSprite(tw, th, 32, 0, 64, 32, 1);
        metal_wall[2] = new TileSprite(tw, th, 64, 0, 96, 32, 2);
        metal_wall[3] = new TileSprite(tw, th, 64, 32, 96, 64, 3);
        metal_wall[4] = new TileSprite(tw, th, 64, 64, 96, 96, 4);
        metal_wall[5] = new TileSprite(tw, th, 32, 64, 64, 96, 5);
        metal_wall[6] = new TileSprite(tw, th, 0, 64, 32, 96, 6);
        metal_wall[7] = new TileSprite(tw, th, 0, 32, 32, 64, 7);
        metal_wall[8] = new TileSprite(tw, th, 32, 32, 64, 64, 8);

        TileSprite[] metal_floor = new TileSprite[9];
        metal_floor[0] = new TileSprite(tw, th, 96, 0, 128, 32, 9);
        metal_floor[1] = new TileSprite(tw, th, 128, 0, 160, 32, 10);
        metal_floor[2] = new TileSprite(tw, th, 160, 0, 192, 32, 11);
        metal_floor[3] = new TileSprite(tw, th, 160, 32, 192, 64, 12);
        metal_floor[4] = new TileSprite(tw, th, 160, 64, 192, 96, 13);
        metal_floor[5] = new TileSprite(tw, th, 128, 64, 160, 96, 14);
        metal_floor[6] = new TileSprite(tw, th, 96, 64, 128, 96, 15);
        metal_floor[7] = new TileSprite(tw, th, 96, 32, 128, 64, 16);
        metal_floor[8] = new TileSprite(tw, th, 128, 32, 160, 64, 17);

        TileSprite[] metal_breakable = new TileSprite[1];
        metal_breakable[0] = new TileSprite(tw, th, 192, 0, 224, 32, 18);

        TileSprite[] metal_player = new TileSprite[1];
        metal_player[0] = new TileSprite(tw, th, 192, 32, 224, 64, 19);

        types.add(new SpriteSet(metal_wall, 1));
        types.add(new SpriteSet(metal_floor, 0));
        types.add(new SpriteSet(metal_breakable, 2));
        types.add(new SpriteSet(metal_player, 3));

        curSpriteSet = types.get(curType);

        int max = Integer.max(tiles.length, tiles[0].length);

        float xScale = 2f/max, yScale = 2f/max, min = Float.min(xScale, yScale), xOffset = -tiles.length*xScale/2, yOffset = -tiles[0].length*yScale/2;
        TileSprite current = curSpriteSet.getCurrent();

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
        return curSpriteSet.getCurrent();
    }

    public void onKeyPressed(int key, int mods)
    {
        super.onKeyPressed(key, mods);

        if(key == Keyboard.KEY_LEFT_BRACKET) onScroll(-1, 0);
        else if(key == Keyboard.KEY_RIGHT_BRACKET) onScroll(1, 0);
        else if(key == Keyboard.KEY_S) save("metal_custom.lvl");
    }

    public void onScroll(float x, float y)
    {
        super.onScroll(x, y);

        if(y < 0) curSpriteSet.addToCurrent(-1);
        else if(y > 0) curSpriteSet.addToCurrent(1);

        if(x < 0) addToSet(-1);
        else if(x > 0) addToSet(1);

        gameObjects.forEach(gameObject ->
        {
            if(!gameObject.getClass().equals(EditorTile.class)) return;

            EditorTile tile = (EditorTile) gameObject;
            if(tile.isMouseOver()) tile.onMouseEntered();
        });
    }
    
    private void addToSet(int amount)
    {
        curType += amount;
        
        if(curType >= types.size()) curType = 0;
        else if(curType < 0) curType = types.size() - 1;
    
        curSpriteSet = types.get(curType);
    }

    private void save(String fileName)
    {
        try(PrintWriter writer = new PrintWriter(fileName))
        {
            writer.println("metal.png\nTILES");
            types.forEach(t ->
            {
                for(TileSprite sprite : t.sprites)
                {
                    writer.println();
                    writer.println(sprite.entry);
                    writer.println(t.id);
                    writer.printf("%d %d %d %d%n", sprite.x0, sprite.y0, sprite.x1, sprite.y1);
                }
            });
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
