package com.pieisnotpi.bomberguy.players;

import com.pieisnotpi.bomberguy.Hitbox;
import com.pieisnotpi.bomberguy.animations.Animation;
import com.pieisnotpi.bomberguy.tiles.GameBoard;
import com.pieisnotpi.engine.input.joystick.Joybind;
import com.pieisnotpi.engine.input.joystick.Xbox;
import com.pieisnotpi.engine.input.keyboard.Keybind;
import com.pieisnotpi.engine.input.keyboard.Keyboard;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.MeshConfig;
import com.pieisnotpi.engine.rendering.shaders.types.tex.TexQuad;
import com.pieisnotpi.engine.scene.GameObject;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector3f;

public class PlayerObject extends GameObject
{
    public static final int maxBombs = 8, maxStrength = 8;

    private Vector3f pos;
    private Joybind joyHor, joyVert, joyPlace;
    private Keybind keyLeft, keyRight, keyUp, keyDown, keyPlace;
    private Animation current;

    private boolean dead = false;
    private int bombStrength = 1, bombCount = 1, controller;
    private GameBoard board;
    private Hitbox hitbox;
    private TexQuad quad;
    private Mesh<TexQuad> mesh;

    public PlayerObject(SpawnPoint spawnPoint, float size, int controller, GameBoard board, Character character/*, Player overseer*/)
    {
        this.board = board;
        this.controller = controller;

        float x = spawnPoint.x(), y = spawnPoint.y();

        transform.setTranslate(x, y, 0);
        pos = transform.pos;

        hitbox = new Hitbox(character.hx0, character.hy0, character.hx1, character.hy1, pos);

        joyHor = new Joybind(controller, Xbox.AXIS_LSTICK_X, false, null, (value, step) -> moveHorizontal(value), () -> current.end());
        joyVert = new Joybind(controller, Xbox.AXIS_LSTICK_Y, false, null, (value, step) -> moveVertical(value), () -> current.end());
        joyPlace = new Joybind(controller, Xbox.BUTTON_A, true, null, null, this::placeBomb);

        if(controller == 10)
        {
            keyLeft = new Keybind(Keyboard.KEY_LEFT, () -> { if(!current.isRunning()) current.start(); }, step -> moveHorizontal(-1), () -> current.end());
            keyRight = new Keybind(Keyboard.KEY_RIGHT, () -> { if(!current.isRunning()) current.start(); }, step -> moveHorizontal(1), () -> current.end());
            keyDown = new Keybind(Keyboard.KEY_DOWN, () -> (current = character.getDownAnimation()).start(), step -> moveVertical(-1), () -> current.end());
            keyUp = new Keybind(Keyboard.KEY_UP, () -> (current = character.getUpAnimation()).start(), step -> moveVertical(1), () -> current.end());
            keyPlace = new Keybind(Keyboard.KEY_ENTER, this::placeBomb, null, null);
        }

        current = character.getDownAnimation();

        mesh = new Mesh<>(character.getMaterial(), MeshConfig.QUAD);
        mesh.addPrimitive(quad = new TexQuad(0, 0, -0.05f, size, size, 0, current.getCurSprite()));
        createRenderable(0, 0, mesh);
    }

    public Hitbox getHitbox()
    {
        return hitbox;
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

        if(controller == 10)
        {
            scene.addKeybind(keyLeft);
            scene.addKeybind(keyRight);
            scene.addKeybind(keyDown);
            scene.addKeybind(keyUp);
            scene.addKeybind(keyPlace);
        }

        scene.addJoybind(joyHor);
        scene.addJoybind(joyVert);
        scene.addJoybind(joyPlace);
    }

    @Override
    public void onUnregister()
    {
        if(controller == 10)
        {
            scene.removeKeybind(keyLeft);
            scene.removeKeybind(keyRight);
            scene.removeKeybind(keyDown);
            scene.removeKeybind(keyUp);
            scene.removeKeybind(keyPlace);
        }

        scene.removeJoybind(joyHor);
        scene.removeJoybind(joyVert);
        scene.removeJoybind(joyPlace);

        super.onUnregister();
    }

    private void placeBomb()
    {
        board.placeBomb((hitbox.x0() + hitbox.x1())/2, (hitbox.y0() + hitbox.y1())/2, this);
    }

    private void moveHorizontal(float dir)
    {
        float move = Math.round(dir);

        if(board.canPlayerMove(move, 0, this))
            transform.translate(move, 0, 0);
        if(board.canPlayerMove(move, 0, this))
            transform.translate(move, 0, 0);
    }

    private void moveVertical(float dir)
    {
        float move = Math.round(dir);

        if(board.canPlayerMove(0, move, this))
            transform.translate(0, move, 0);
        if(board.canPlayerMove(0, move, this))
            transform.translate(0, move, 0);
    }
}
