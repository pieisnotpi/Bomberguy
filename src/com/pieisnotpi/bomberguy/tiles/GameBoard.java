package com.pieisnotpi.bomberguy.tiles;

import com.pieisnotpi.bomberguy.Bomb;
import com.pieisnotpi.bomberguy.Hitbox;
import com.pieisnotpi.bomberguy.maps.GameMap;
import com.pieisnotpi.bomberguy.players.Character;
import com.pieisnotpi.bomberguy.players.PlayerObject;
import com.pieisnotpi.bomberguy.shaders.CharMaterial;
import com.pieisnotpi.bomberguy.upgrades.BombCountUpgrade;
import com.pieisnotpi.bomberguy.upgrades.StrengthUpgrade;
import com.pieisnotpi.bomberguy.upgrades.Upgrade;
import com.pieisnotpi.engine.input.keyboard.Keyboard;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexMaterial;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.GameObject;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard extends GameObject
{
    private static final TexMaterial bombMaterial = new TexMaterial(Camera.ORTHO2D_R, Texture.getTextureFile("bomb.png"));
    private static final CharMaterial upgradesMaterial = new CharMaterial(Camera.ORTHO2D_R, Texture.getTextureFile("upgrades.png"));

    private GameMap map;
    private Mesh<TexQuad> nonstaticMesh, bombsMesh, upgradesMesh;
    private List<Bomb> bombs;
    private List<PlayerObject> players;
    private List<Upgrade> upgrades;
    private Random random;

    public GameBoard(GameMap map)
    {
        this.map = map;

        players = new ArrayList<>();
        bombs = new ArrayList<>();
        upgrades = new ArrayList<>();
        random = new Random();

        nonstaticMesh = map.genNonstaticMesh();
        bombsMesh = new Mesh<>(bombMaterial, MeshConfig.QUAD);
        upgradesMesh = new Mesh<>(upgradesMaterial, MeshConfig.QUAD);

        map.genMapForBoard();

        transform.setCenter(map.getResWidth()/2f, map.getResHeight()/2f, 0);

        createRenderable(0, 0, map.genStaticMesh(), nonstaticMesh, bombsMesh, upgradesMesh);
    }

    public void loadMap(GameMap map)
    {
        if(this.map == map) return;

        this.map = map;
        players.forEach(this::removeChild);
        players.clear();
        bombs.clear();
        upgrades.clear();

        nonstaticMesh.destroy();
        bombsMesh.destroy();
        upgradesMesh.destroy();
        scene.removeRenderable(renderable);
        renderable = null;

        nonstaticMesh = map.genNonstaticMesh();
        bombsMesh = new Mesh<>(bombMaterial, MeshConfig.QUAD);
        upgradesMesh = new Mesh<>(upgradesMaterial, MeshConfig.QUAD);

        createRenderable(0, 0, map.genStaticMesh(), nonstaticMesh, bombsMesh, upgradesMesh);
        transform.setCenter(map.getResWidth()/2f, map.getResHeight()/2f, 0);

        map.genMapForBoard();

        onWindowResize(scene.window.getBufferRes());
    }

    public void gameUpdate(float timeStep)
    {
        for(int i = 0; i < bombs.size(); i++)
        {
            Bomb b = bombs.get(i);
            if(b.isExploded(timeStep))
            {
                explodeAtPoint(b.tx(), b.ty(), b.getStrength());

                bombsMesh.removePrimitive(b.getQuad());
                bombs.remove(i);
                i--;
            }
            if(b.spriteUpdated(timeStep)) bombsMesh.flagForBuild();
        }

        for (int i = 0; i < players.size(); i++)
        {
            PlayerObject player = players.get(i);
            if(!player.isRegistered() && !player.isDead()) addChild(player);
            if(player.isDead() && player.isRegistered())
            {
                players.remove(i);
                removeChild(player);
                i--;
            }
        }
    }

    public void placeBomb(float px, float py, PlayerObject player)
    {
        int allowed = player.getBombCount(), strength = player.getBombStrength(), found = 0;

        for(Bomb bomb : bombs) if(bomb.owner == player) found++;

        if(found >= allowed) return;

        int tx = map.xScreenToTile(px), ty = map.yScreenToTile(py);
        float x = map.xTileToScreen(tx), y = map.yTileToScreen(ty);

        for(Bomb bomb : bombs)
        {
            if(bomb.getHitbox().isWithin(x, y, x + map.tileSize(), y + map.tileSize())) return;
        }

        Bomb b = new Bomb(tx, ty, x, y, map.tileSize(), strength, player);
        bombsMesh.addPrimitive(b.getQuad());
        bombs.add(b);
    }

    //TODO This needs to goooooooo
    @Override
    public void onKeyPressed(int key, int mods)
    {
        super.onKeyPressed(key, mods);

        if(key == Keyboard.KEY_SPACE)
        {
            if(players.size() == 0) addPlayer(0);
            else if(players.get(0).isDead()) players.get(0).unkill();
        }
    }

    @Override
    public void onWindowResize(Vector2i res)
    {
        super.onWindowResize(res);

        int rw = map.getResWidth(), rh = map.getResHeight();

        int scale = Integer.max(1, Integer.min(res.x/rw, res.y/rh));

        int cx = res.x/2, cy = res.y/2;
        int crx = rw/2, cry = rh/2;

        getTransform().setScaleCentered(1);
        getTransform().setTranslateAbs(cx - crx, cy - cry, 0);
        getTransform().setScaleCentered(scale);
    }

    public boolean canPlayerMove(float xShift, float yShift, PlayerObject player)
    {
        Hitbox box = player.getHitbox();

        float x0 = box.x0() + xShift, x1 = box.x1() + xShift, y0 = box.y0() + yShift, y1 = box.y1() + yShift;

        if(map.tileAt(x0, y0) != null || map.tileAt(x1, y1) != null) return false;
        if(map.tileAt(x0, y1) != null || map.tileAt(x1, y0) != null) return false;

        for (Bomb bomb : bombs)
        {
            if(box.collidesWith(xShift, yShift, bomb.getHitbox()))
            {
                if(bomb.owner != player || bomb.escaped) return false;
            }
            else if(bomb.owner == player)
            {
                bomb.escaped = true;
            }
        }

        for (int i = 0; i < upgrades.size(); i++)
        {
            Upgrade u = upgrades.get(i);
            if(box.collidesWith(xShift, yShift, u.getHitbox()) && u.onCollect(player))
            {
                upgrades.remove(u);
                upgradesMesh.removePrimitive(u.getQuad());
                i--;
            }
        }
        return true;
    }

    public PlayerObject addPlayer(int controller)
    {
        return addPlayer(controller, Character.carl);
    }

    public PlayerObject addPlayer(int controller, Character character)
    {
        int spawnPoint = players.size();
        if(spawnPoint >= map.spawnPoints().size()) return null;

        PlayerObject player = new PlayerObject(map.spawnPoints().get(spawnPoint), map.tileSize(), controller, this, character);
        players.add(player);
        return player;
    }

    private void explodeAtPoint(int tx, int ty, int strength)
    {
        for(int x = tx; x <= tx + strength && x < map.width(); x++)
        {
            GameTile tile = map.tileAt(x, ty);
            if (tile == null) explodeEmptyZone(x, ty);
            else if (tile.getClass().equals(WallTile.class)) break;
            else explodeTile(x, ty, tile);
        }

        for(int x = tx - 1; x >= tx - strength && x >= 0; x--)
        {
            GameTile tile = map.tileAt(x, ty);
            if (tile == null) explodeEmptyZone(x, ty);
            else if (tile.getClass().equals(WallTile.class)) break;
            else explodeTile(x, ty, tile);
        }

        for(int y = ty + 1; y <= ty + strength && y < map.height(); y++)
        {
            GameTile tile = map.tileAt(tx, y);
            if (tile == null) explodeEmptyZone(tx, y);
            else if (tile.getClass().equals(WallTile.class)) break;
            else explodeTile(tx, y, tile);
        }

        for(int y = ty - 1; y >= ty - strength && y >= 0; y--)
        {
            GameTile tile = map.tileAt(tx, y);
            if (tile == null) explodeEmptyZone(tx, y);
            else if (tile.getClass().equals(WallTile.class)) break;
            else explodeTile(tx, y, tile);
        }
    }

    private void explodeEmptyZone(int tx, int ty)
    {
        float x0 = map.xTileToScreen(tx), y0 = map.yTileToScreen(ty),
                x1 = x0 + map.tileSize(), y1 = y0 + map.tileSize();

        for (PlayerObject player : players)
        {
            if (player.getHitbox().isWithin(x0, y0, x1, y1)) explodePlayer(player);
        }

        for (int i = 0; i < upgrades.size(); i++)
        {
            Upgrade u = upgrades.get(i);
            if (u.getHitbox().isWithin(x0, y0, x1, y1))
            {
                upgrades.remove(u);
                upgradesMesh.removePrimitive(u.getQuad());
                i--;
            }
        }
    }

    private void explodeTile(int tx, int ty, GameTile tile)
    {
        map.nullifyTile(tx, ty);
        nonstaticMesh.removePrimitive(tile.getQuad());

        int dropOdds = randomInt(0, 100);
        if(dropOdds <= BombCountUpgrade.odds)
        {
            BombCountUpgrade u = new BombCountUpgrade(map.tileSize(), new Vector3f(map.xTileToScreen(tx), map.yTileToScreen(ty), 0));
            upgrades.add(u);
            upgradesMesh.addPrimitive(u.getQuad());
        }
        else if(dropOdds <= StrengthUpgrade.odds)
        {
            StrengthUpgrade u = new StrengthUpgrade(map.tileSize(), new Vector3f(map.xTileToScreen(tx), map.yTileToScreen(ty), 0));
            upgrades.add(u);
            upgradesMesh.addPrimitive(u.getQuad());
        }
    }

    private void explodePlayer(PlayerObject player)
    {
        //TODO game code
        player.kill();
    }

    private int randomInt(int min, int max)
    {
        return random.nextInt(max - min + 1) + min;
    }
}
