package com.pieisnotpi.bomberguy.menu;

import com.pieisnotpi.bomberguy.maps.GameMap;
import com.pieisnotpi.bomberguy.players.Character;
import com.pieisnotpi.bomberguy.players.Player;
import com.pieisnotpi.engine.input.joystick.Joybind;
import com.pieisnotpi.engine.input.joystick.Joystick;
import com.pieisnotpi.engine.input.joystick.Xbox;
import com.pieisnotpi.engine.scene.GameObject;

import java.util.List;

public class MainMenu extends GameObject
{
    private GameMap nextLevel;
    private List<Player> players;
    private List<Joybind> joybinds;
    private Character[] chars = { Character.robot, Character.carl };

    public MainMenu(List<Player> players)
    {
        this.players = players;
    }

    @Override
    public void onJoystickConnect(Joystick joystick)
    {
        super.onJoystickConnect(joystick);

        int id = joystick.joyID;
        Joybind lStick = new Joybind(id, Xbox.AXIS_LSTICK_X, false, null, (value, timeStep) ->
        {
            Player p = players.get(players.size() - 1);
            changeChar(Math.round(value), p);
        }, null);

        joybinds.add(lStick);
    }

    public void changeChar(int dir, Player player)
    {
        if(dir < 0) player.place--;
        else if (dir > 0) player.place++;


    }

    public void setNextLevel(GameMap next)
    {
        nextLevel = next;
    }
}
