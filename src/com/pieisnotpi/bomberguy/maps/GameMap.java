package com.pieisnotpi.bomberguy.maps;

import com.pieisnotpi.bomberguy.physics.Hitbox;
import com.pieisnotpi.bomberguy.physics.PhysicsContainer;
import com.pieisnotpi.bomberguy.physics.StaticPhysicsObject;
import com.pieisnotpi.bomberguy.players.SpawnPoint;
import com.pieisnotpi.bomberguy.tiles.*;
import com.pieisnotpi.engine.PiEngine;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GameMap
{
    private GameTile[][] mapInternal;
    private GameTile[][] map;
    private StaticPhysicsObject[][] mapHitboxes;

    private int tw, th, rw, rh;
    private float xOffset, yOffset, size;
    private List<TexQuad> nonstaticQuads;
    private List<SpawnPoint> spawnPoints;
    private Mesh<TexQuad> staticMesh;
    private TexMaterial material;
    private String title;

    public GameMap(File file, String title)
    {
        this.title = title;

        Scanner scanner;

        try
        {
            scanner = new Scanner(file);
        }
        catch (FileNotFoundException e)
        {
            PiEngine.closeWithException(e);
            return;
        }

        Texture texture = Texture.getTextureFile(scanner.nextLine());
        material = new TexMaterial(Camera.ORTHO2D_R, texture);

        Map<Integer, TileEntry> db = new HashMap<>();
        spawnPoints = new ArrayList<>();
        staticMesh = new Mesh<>(material, MeshConfig.QUAD_STATIC);
        nonstaticQuads = new ArrayList<>();

        while (!scanner.nextLine().equals("END"))
        {
            int entry = scanner.nextInt();
            int type = scanner.nextInt();
            int x0 = scanner.nextInt(), y0 = scanner.nextInt(), x1 = scanner.nextInt(), y1 = scanner.nextInt();
            if(entry != -1) db.put(entry, new TileEntry(type, new Sprite(texture, x0, y0, x1, y1)));
            scanner.nextLine();
        }

        tw = scanner.nextInt();
        th = scanner.nextInt();

        rw = tw*32;
        rh = th*32;

        mapInternal = new GameTile[tw][th];
        map = new GameTile[tw][th];
        mapHitboxes = new StaticPhysicsObject[tw][th];

        xOffset = 0;
        yOffset = 0;
        size = 32;

        for(int ty = th - 1; ty >= 0; ty--)
        {
            for(int tx = 0; tx < tw; tx++)
            {
                float x = xOffset + size*tx, y = yOffset + size*ty;

                int entryVal = scanner.nextInt();
                if(entryVal == -1) continue;
                TileEntry entry = db.get(entryVal);

                staticMesh.addPrimitive(new TexQuad(x, y, -0.15f, size, size, 0, entry.getSprite()));
            }
        }

        for(int ty = th - 1; ty >= 0; ty--)
        {
            for(int tx = 0; tx < tw; tx++)
            {
                float x = xOffset + size*tx, y = yOffset + size*ty;

                int entryVal = scanner.nextInt();
                if(entryVal == -1) continue;

                TileEntry entry = db.get(entryVal);
                int type = entry.getTileType();

                if(type == 1)
                {
                    GameTile g = mapInternal[tx][ty] = new WallTile(x, y, size, entry.getSprite());
                    staticMesh.addPrimitive(g.getQuad());
                }
                else if(type == 2)
                {
                    GameTile b = mapInternal[tx][ty] = new BreakableTile(x, y, size, entry.getSprite());
                    nonstaticQuads.add(b.getQuad());
                }
                else if(type == 3)
                {
                    spawnPoints.add(new SpawnPoint(tx, ty, x, y));
                }
            }
        }
    }

    public void genMapForBoard(PhysicsContainer physContainer)
    {
        for(int tx = 0; tx < tw; tx++)
        {
            if (th >= 0) System.arraycopy(mapInternal[tx], 0, map[tx], 0, th);
            for (int ty = 0; ty < th; ty++)
            {
                if (mapInternal[tx][ty] != null)
                {
                    mapHitboxes[tx][ty] = new StaticPhysicsObject(xTileToScreen(tx), yTileToScreen(ty), xTileToScreen(tx + 1), yTileToScreen(ty + 1));
                    physContainer.addPhysicsObject(0, mapHitboxes[tx][ty]);
                }
            }
        }
    }

    public void nullifyTile(int tx, int ty)
    {
        map[tx][ty] = null;
        mapHitboxes[tx][ty] = null;
    }

    public GameTile tileAt(float x, float y)
    {
        int tx = xScreenToTile(x);
        int ty = yScreenToTile(y);

        return map[tx][ty];
    }

    public GameTile tileAt(int tx, int ty)
    {
        return map[tx][ty];
    }

    public StaticPhysicsObject hitboxAt(int tx, int ty)
    {
        return mapHitboxes[tx][ty];
    }

    public Mesh<TexQuad> genStaticMesh()
    {
        return staticMesh;
    }

    public Mesh<TexQuad> genNonstaticMesh()
    {
        Mesh<TexQuad> mesh = new Mesh<>(material, MeshConfig.QUAD);
        nonstaticQuads.forEach(mesh::addPrimitive);
        return mesh;
    }

    public List<SpawnPoint> spawnPoints()
    {
        return spawnPoints;
    }

    public TexMaterial getMaterial() { return material; }

    public boolean objectCollidesWithMap(float xShift, float yShift, Hitbox box)
    {
        float x0 = box.x0() + xShift, x1 = box.x1() + xShift, y0 = box.y0() + yShift, y1 = box.y1() + yShift;

        if(tileAt(x0, y0) != null || tileAt(x1, y1) != null) return true;
        return tileAt(x0, y1) != null || tileAt(x1, y0) != null;
    }

    public int getResWidth()
    {
        return rw;
    }

    public int getResHeight()
    {
        return rh;
    }

    /**
     * Converts x tile coordinate to screen/world coordinate
     * @param tx X tile coordinate
     * @return X screen coordinate
     */
    public float xTileToScreen(int tx)
    {
        return xOffset + size*tx;
    }

    /**
     * Converts y tile coordinate to screen/world coordinate
     * @param ty Y tile coordinate
     * @return Y screen coordinate
     */
    public float yTileToScreen(int ty)
    {
        return yOffset + size*ty;
    }

    /**
     * Converts x screen coordinate to tile coordinate
     * @param x X screen coordinate
     * @return X tile coordinate
     */
    public int xScreenToTile(float x)
    {
        return (int) Math.floor((x - xOffset)/size);
    }

    /**
     * Converts y screen coordinate to tile coordinate
     * @param y Y screen coordinate
     * @return Y tile coordinate
     */
    public int yScreenToTile(float y)
    {
        return (int) Math.floor((y - yOffset)/size);
    }

    public int width()
    {
        return tw;
    }

    public int height()
    {
        return th;
    }

    public float tileSize()
    {
        return size;
    }

    public String getTitle()
    {
        return title;
    }
}
