package com.pieisnotpi.bomberguy.players;

import com.pieisnotpi.bomberguy.physics.DynamicPhysicsObject;
import com.pieisnotpi.bomberguy.physics.Hitbox;
import com.pieisnotpi.bomberguy.animations.Animation;
import com.pieisnotpi.bomberguy.shaders.CharMaterial;
import com.pieisnotpi.bomberguy.tiles.GameBoard;
import com.pieisnotpi.engine.input.joystick.Joybind;
import com.pieisnotpi.engine.input.joystick.Xbox;
import com.pieisnotpi.engine.input.keyboard.Keybind;
import com.pieisnotpi.engine.input.keyboard.Keyboard;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexQuad;
import com.pieisnotpi.engine.rendering.textures.Sprite;
import com.pieisnotpi.engine.scene.Scene;

public class PlayerObject extends DynamicPhysicsObject
{
    public static final int MAX_BOMBS = 8, MAX_STRENGTH = 8;
    public static final float MOVE_AMOUNT = 80;

    private Joybind joyHor, joyVert, joyPlace;
    private Keybind keyLeft, keyRight, keyUp, keyDown, keyPlace;
    private Animation current;

    private boolean dead = false;
    private int bombStrength = 1, bombCount = 1, controller;
    private GameBoard board;
    private Hitbox hitbox;
    private TexQuad quad;
    private Mesh<TexQuad> mesh;
    private CharMaterial material;

    public PlayerObject(SpawnPoint spawnPoint, float size, int controller, GameBoard board, Character character/*, Player overseer*/)
    {
        this.board = board;
        this.controller = controller;

        collidesWith = new int[]{0, 2};
        bounciness = 0;

        float x = spawnPoint.x(), y = spawnPoint.y();

        transform.setTranslate(x, y, 0);

        hitbox = new Hitbox(character.hx0, character.hy0, character.hx1, character.hy1, transform.pos);

        joyHor = new Joybind(controller, Xbox.AXIS_LSTICK_X, false, null, this::moveHorizontal, () -> current.end());
        joyVert = new Joybind(controller, Xbox.AXIS_LSTICK_Y, false, null, this::moveVertical, () -> current.end());
        joyPlace = new Joybind(controller, Xbox.BUTTON_A, true, null, null, this::placeBomb);

        if(controller == -1)
        {
            keyLeft = new Keybind(Keyboard.KEY_LEFT, () -> { if(!current.isRunning()) current.start(); }, step -> moveHorizontal(-1, step), this::stopMovingX);
            keyRight = new Keybind(Keyboard.KEY_RIGHT, () -> { if(!current.isRunning()) current.start(); }, step -> moveHorizontal(1, step), this::stopMovingX);
            keyDown = new Keybind(Keyboard.KEY_DOWN, () -> (current = character.getDownAnimation()).start(), step -> moveVertical(-1, step), this::stopMovingY);
            keyUp = new Keybind(Keyboard.KEY_UP, () -> (current = character.getUpAnimation()).start(), step -> moveVertical(1, step), this::stopMovingY);
            keyPlace = new Keybind(Keyboard.KEY_ENTER, this::placeBomb, null, null);
        }

        current = character.getDownAnimation();

        mesh = new Mesh<>(material = character.getMaterial(), MeshConfig.QUAD);
        mesh.addPrimitive(quad = new TexQuad(0, 0, -0.05f, size, size, 0, current.getCurSprite()));
        createRenderable(0, 0, mesh);
    }

    private void stopMovingX()
    {
        current.end();
        velocity.x = 0;
    }

    private void stopMovingY()
    {
        current.end();
        velocity.y = 0;
    }

    public Hitbox getHitbox()
    {
        return hitbox;
    }

    public Sprite getSprite()
    {
        return quad.getQuadSprite();
    }

    public CharMaterial getMaterial()
    {
        return material;
    }

    public int getBombStrength()
    {
        return bombStrength;
    }

    public int getBombCount()
    {
        return bombCount;
    }

    public boolean isDead()
    {
        return dead;
    }

    public void upgradeStrength()
    {
        bombStrength++;
    }

    public void upgradeCount()
    {
        bombCount++;
    }

    public void kill()
    {
        dead = true;
    }

    public void unkill()
    {
        dead = false;
    }

    @Override
    public void drawUpdate(float timeStep)
    {
        super.drawUpdate(timeStep);

        if(current.hasSpriteChanged(timeStep))
        {
            quad.setQuadSprite(current.getCurSprite());
            mesh.flagForBuild();
        }
    }

    @Override
    public void onRegister(Scene scene)
    {
        super.onRegister(scene);

        if(controller == -1)
        {
            scene.addKeybind(keyLeft);
            scene.addKeybind(keyRight);
            scene.addKeybind(keyDown);
            scene.addKeybind(keyUp);
            scene.addKeybind(keyPlace);
        }
        else
        {
            scene.addJoybind(joyHor);
            scene.addJoybind(joyVert);
            scene.addJoybind(joyPlace);
        }
    }

    @Override
    public void onUnregister()
    {
        if(controller == -1)
        {
            scene.removeKeybind(keyLeft);
            scene.removeKeybind(keyRight);
            scene.removeKeybind(keyDown);
            scene.removeKeybind(keyUp);
            scene.removeKeybind(keyPlace);
        }
        else
        {
            scene.removeJoybind(joyHor);
            scene.removeJoybind(joyVert);
            scene.removeJoybind(joyPlace);
        }

        if(scene != null && renderable != null) scene.removeRenderable(renderable);
        if(node != null) node.removeSelf();

        scene = null;
    }

    private void placeBomb()
    {
        board.placeBomb((hitbox.x0() + hitbox.x1())/2, (hitbox.y0() + hitbox.y1())/2, this);
    }

    private void moveHorizontal(float dir, float timeStep)
    {
        velocity.x = MOVE_AMOUNT*Math.round(dir);
        /*float move = 120*Math.round(dir)*timeStep;

        for (int i = 0; i < moveSteps; i++)
        {
            if(board.canPlayerMove(move/moveSteps, 0, this))
                transform.translate(move/moveSteps, 0, 0);
        }*/
    }

    private void moveVertical(float dir, float timeStep)
    {
        velocity.y = MOVE_AMOUNT*Math.round(dir);

        /*float move = 120*Math.round(dir)*timeStep;

        for (int i = 0; i < moveSteps; i++)
        {
            if(board.canPlayerMove(0, move/moveSteps, this))
                transform.translate(0, move/moveSteps, 0);
        }*/
    }
}
