package com.pieisnotpi.bomberguy.players;

import com.pieisnotpi.bomberguy.tiles.GameBoard;

public class Player
{
    public int place = 0;

    private int kills = 0, wins = 0, joystick;
    private Character character;
    private PlayerObject playerObject;

    public Player(int joystick, Character character)
    {
        this.joystick = joystick;
        this.character = character;
    }

    public void setCharacter(Character character)
    {
        this.character = character;
    }

    public void addToGame(GameBoard board)
    {
        playerObject = board.addPlayer(joystick, character);
    }
}
