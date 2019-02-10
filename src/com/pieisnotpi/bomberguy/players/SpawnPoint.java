package com.pieisnotpi.bomberguy.players;

public class SpawnPoint
{
    private int tx, ty;
    private float x, y;

    public SpawnPoint(int tx, int ty, float x, float y)
    {
        this.tx = tx;
        this.ty = ty;
        this.x = x;
        this.y = y;
    }

    public int tx()
    {
        return tx;
    }

    public int ty()
    {
        return ty;
    }

    public float x()
    {
        return x;
    }

    public float y()
    {
        return y;
    }
}
