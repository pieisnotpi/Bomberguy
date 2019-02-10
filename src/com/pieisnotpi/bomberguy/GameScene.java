package com.pieisnotpi.bomberguy;

import com.pieisnotpi.bomberguy.maps.GameMaps;
import com.pieisnotpi.bomberguy.menu.MainMenu;
import com.pieisnotpi.bomberguy.players.Character;
import com.pieisnotpi.bomberguy.players.Player;
import com.pieisnotpi.bomberguy.tiles.GameBoard;
import com.pieisnotpi.engine.input.joystick.Joystick;
import com.pieisnotpi.engine.input.keyboard.Keyboard;
import com.pieisnotpi.engine.rendering.cameras.Camera;
import com.pieisnotpi.engine.scene.Scene;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class GameScene extends Scene
{
    private GameBoard board;
    private MainMenu menu;
    private List<Player> players;
    private boolean inMenu = true;

    @Override
    public Scene init() throws Exception
    {
        super.init();

        clearColor.set(0.55f, 0.6f, 0.7f);
        players = new ArrayList<>();

        addCamera(new Camera(1, new Vector2f(0, 0), new Vector2f(1, 1)));

        board = new GameBoard(GameMaps.metal6);
        addGameObject(board);
        board.addPlayer(10, Character.robot);

        return this;
    }

    @Override
    public void onJoystickConnect(Joystick joystick)
    {
        Player p = new Player(joystick.joyID, Character.carl);
        players.add(p);
        p.addToGame(board);
        super.onJoystickConnect(joystick);
    }

    @Override
    public void update(float timeStep) throws Exception
    {
        super.update(timeStep);

        board.gameUpdate(timeStep);
    }

    @Override
    public void onKeyPressed(int key, int mods)
    {
        super.onKeyPressed(key, mods);

        if(key == Keyboard.KEY_1) board.loadMap(GameMaps.metal1);
        if(key == Keyboard.KEY_2) board.loadMap(GameMaps.metal2);
        if(key == Keyboard.KEY_3) board.loadMap(GameMaps.metal3);
        if(key == Keyboard.KEY_4) board.loadMap(GameMaps.metal4);
        if(key == Keyboard.KEY_5) board.loadMap(GameMaps.metal5);
        if(key == Keyboard.KEY_6) board.loadMap(GameMaps.metal6);
    }
}
