package com.pieisnotpi.bomberguy.tiles;

import com.pieisnotpi.bomberguy.Bomb;
import com.pieisnotpi.bomberguy.physics.DynamicPhysicsObject;
import com.pieisnotpi.bomberguy.physics.Hitbox;
import com.pieisnotpi.bomberguy.maps.GameMap;
import com.pieisnotpi.bomberguy.physics.PhysicsContainer;
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
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.rendering.textures.Texture;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.utility.Color;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.*;

public class GameBoard extends GameObject
{
    private static final CharMaterial upgradesMaterial =
            new CharMaterial(Camera.ORTHO2D_R, Texture.getTextureFile("upgrades.png"));

    private static final float TILE_PARTICLE_SPEED = 200, PLAYER_PARTICLE_SPEED = 240, ITEM_PARTICLE_SPEED = 160;

    private GameMap map;
    private Mesh<TexQuad> nonstaticMesh, upgradesMesh;
    private List<Bomb> bombs;
    private List<PlayerObject> players;
    private List<Upgrade> upgrades;
    private PhysicsContainer physics;
    private Random random;

    public GameBoard(GameMap map)
    {
        this.map = map;

        players = new ArrayList<>();
        bombs = new ArrayList<>();
        upgrades = new ArrayList<>();
        physics = new PhysicsContainer();
        random = new Random();

        nonstaticMesh = map.genNonstaticMesh();
        upgradesMesh = new Mesh<>(upgradesMaterial, MeshConfig.QUAD);

        map.genMapForBoard(physics);

        transform.setCenter(map.getResWidth()/2f, map.getResHeight()/2f, 0);

        createRenderable(0, 0, map.genStaticMesh(), nonstaticMesh, upgradesMesh);
    }

    public void loadMap(GameMap map)
    {
        if(this.map == map) return;

        this.map = map;
        players.forEach(p -> removeChild(1, p));
        players.clear();
        bombs.clear();
        upgrades.clear();
        physics.clear();

        nonstaticMesh.destroy();
        upgradesMesh.destroy();
        scene.removeRenderable(renderable);
        renderable = null;

        nonstaticMesh = map.genNonstaticMesh();
        upgradesMesh = new Mesh<>(upgradesMaterial, MeshConfig.QUAD);

        createRenderable(0, 0, map.genStaticMesh(), nonstaticMesh, upgradesMesh);
        transform.setCenter(map.getResWidth()/2f, map.getResHeight()/2f, 0);

        map.genMapForBoard(physics);

        onWindowResize(scene.window.getBufferRes());
    }

    @Override
    public void update(float timeStep)
    {
        physics.simulate(timeStep);

        bombs.forEach(bomb -> bomb.setCollideable(players.stream().noneMatch(player ->
                bomb.getHitbox().collidesWith(0, 0, player.getHitbox()))));

        players.forEach(player ->
        {
            for (int i = 0; i < upgrades.size(); i++)
            {
                Upgrade u = upgrades.get(i);
                if(player.getHitbox().collidesWith(0, 0, u.getHitbox()) && u.onCollect(player))
                {
                    upgrades.remove(u);
                    upgradesMesh.removePrimitive(u.getQuad());
                    i--;
                }
            }
        });
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

        Bomb b = new Bomb(tx, ty, x, y, map.tileSize(), strength, player, this);
        bombs.add(b);
        addChild(2, b);
    }

    //TODO This needs to goooooooo
    @Override
    public void onKeyPressed(int key, int mods)
    {
        super.onKeyPressed(key, mods);

        if(key == Keyboard.KEY_SPACE)
        {
            if(players.size() == 0) addPlayer(-1);
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
        addChild(1, player);
        return player;
    }

    public PhysicsContainer getPhysics()
    {
        return physics;
    }

    public void detonateBomb(Bomb bomb)
    {
        int tx = bomb.tx(), ty = bomb.ty();

        for(int x = tx; x <= tx + bomb.getStrength() && x < map.width(); x++)
        {
            GameTile tile = map.tileAt(x, ty);
            if (tile == null) explodeEmptyZone(x, ty, tx, ty);
            else if (tile.getClass().equals(WallTile.class)) break;
            else explodeTile(x, ty, TILE_PARTICLE_SPEED/(x - tx), 0, tile);
        }

        for(int x = tx - 1; x >= tx - bomb.getStrength() && x >= 0; x--)
        {
            GameTile tile = map.tileAt(x, ty);
            if (tile == null) explodeEmptyZone(x, ty, tx, ty);
            else if (tile.getClass().equals(WallTile.class)) break;
            else explodeTile(x, ty, TILE_PARTICLE_SPEED/(x - tx), 0, tile);
        }

        for(int y = ty + 1; y <= ty + bomb.getStrength() && y < map.height(); y++)
        {
            GameTile tile = map.tileAt(tx, y);
            if (tile == null) explodeEmptyZone(tx, y, tx, ty);
            else if (tile.getClass().equals(WallTile.class)) break;
            else explodeTile(tx, y, 0, TILE_PARTICLE_SPEED/(y - ty), tile);
        }

        for(int y = ty - 1; y >= ty - bomb.getStrength() && y >= 0; y--)
        {
            GameTile tile = map.tileAt(tx, y);
            if (tile == null) explodeEmptyZone(tx, y, tx, ty);
            else if (tile.getClass().equals(WallTile.class)) break;
            else explodeTile(tx, y, 0, TILE_PARTICLE_SPEED/(y - ty), tile);
        }

        bombs.remove(bomb);
        physics.removePhysicsObject(2, bomb);

        physics.applyInstantForce(3, bomb.getHitbox().mx(), bomb.getHitbox().my(), (bomb.getStrength() + 0.5f) * map.tileSize(), 25);
    }

    private void explodeEmptyZone(int tx, int ty, int ox, int oy)
    {
        float tileSize = map.tileSize();
        float x0 = map.xTileToScreen(tx), y0 = map.yTileToScreen(ty), x1 = x0 + tileSize, y1 = y0 + tileSize;

        float bombMidX = map.xTileToScreen(ox) + tileSize/2, bombMidY = map.yTileToScreen(oy) + tileSize/2;

        for (int i = 0; i < players.size(); i++)
        {
            PlayerObject player = players.get(i);

            if (player.getHitbox().isWithin(x0, y0, x1, y1))
            {
                Hitbox ph = player.getHitbox();
                float px = ph.x0(), py = ph.y0();
                float playerMidX = px + (ph.x1() - px) / 2;
                float playerMidY = py + (ph.y1() - py) / 2;

                double angle = Math.atan2(playerMidY - bombMidY, playerMidX - bombMidX);
                float vx = PLAYER_PARTICLE_SPEED * (float) Math.cos(angle);
                float vy = PLAYER_PARTICLE_SPEED * (float) Math.sin(angle);

                explodePlayer(player);
                i--;

                Sprite sprite = player.getSprite();
                CharMaterial m = player.getMaterial();

                particlize(px - 8, py, vx, vy, 8, 0, 24, 32, 0, 5f, m.textures[0], sprite, new Color(1, 0, 0, 1));
            }
        }

        for (int i = 0; i < upgrades.size(); i++)
        {
            Upgrade u = upgrades.get(i);
            if (u.getHitbox().isWithin(x0, y0, x1, y1))
            {
                upgrades.remove(u);
                upgradesMesh.removePrimitive(u.getQuad());
                i--;

                Sprite sprite = u.getQuad().getQuadSprite();

                float sx = map.xTileToScreen(tx), sy = map.yTileToScreen(ty);
                float vx = tx == ox ? 0 : ITEM_PARTICLE_SPEED /(tx - ox), vy = ty == oy ? 0 : ITEM_PARTICLE_SPEED /(ty - oy);

                particlize(sx, sy, vx, vy, 8, 8, 22, 22, 0.25f, 2f, upgradesMaterial.textures[0], sprite, new Color(0, 0, 0, 1));
            }
        }
    }

    private void explodeTile(int tx, int ty, float vx, float vy, GameTile tile)
    {
        physics.removePhysicsObject(0, map.hitboxAt(tx, ty));
        map.nullifyTile(tx, ty);
        nonstaticMesh.removePrimitive(tile.getQuad());

        Sprite sprite = tile.getQuad().getQuadSprite();

        float sx = map.xTileToScreen(tx), sy = map.yTileToScreen(ty);
        TexMaterial m = map.getMaterial();

        particlize(sx, sy, vx, vy, 0, 0, 32, 32, 0.25f, 1f, m.textures[0], sprite, new Color(0, 0, 0, 1));

        int dropOdds = randomUpgrade();
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

    private void particlize(float screenX, float screenY, float vx, float vy, int startX, int startY, int endX, int endY, float bounciness, float mass, Texture texture, Sprite sprite, Color color)
    {
        int texX = sprite.x0, texY = sprite.y0;
        float vxRanMult = vx == 0 ? vy : vx/2;
        float vyRanMult = vy == 0 ? vx : vy/2;

        for (int x = startX; x < endX; x += 2)
        {
            for (int y = startY; y < endY; y += 2)
            {
                float vxOffset = (random.nextFloat() - 0.5f) * vxRanMult;
                float vyOffset = (random.nextFloat() - 0.5f) * vyRanMult;

                TileParticle particle = new TileParticle(screenX + x, screenY + endY - y, 2, texture, 2, texX + x, texY + y, vxOffset, vyOffset, color, this);
                particle.setBounciness(bounciness);
                particle.setMass(mass);
                addChild(3, particle);
            }
        }
    }

    private void explodePlayer(PlayerObject player)
    {
        //TODO game code
        player.kill();
        players.remove(player);
        removeChild(1, player);
    }

    private int randomUpgrade()
    {
        return random.nextInt(100) + 1;
    }

    private void addChild(int layer, DynamicPhysicsObject physObj)
    {
        super.addChild(physObj);
        physics.addPhysicsObject(layer, physObj);
    }

    private void removeChild(int layer, DynamicPhysicsObject physObj)
    {
        super.removeChild(physObj);
        physics.removePhysicsObject(layer, physObj);
    }
}
