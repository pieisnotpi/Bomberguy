package com.pieisnotpi.bomberguy.physics;

import org.joml.Vector3f;

public class Hitbox
{
    private float x0, y0, x1, y1, mx, my;
    private Vector3f pos;

    public Hitbox(float x0, float y0, float x1, float y1, Vector3f pos)
    {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;

        mx = x0 + (x1 - x0) / 2f;
        my = y0 + (y1 - y0) / 2f;

        this.pos = pos;
    }

    public boolean collidesWith(float xOffset, float yOffset, Hitbox hitbox)
    {
        float nx0 = x0() + xOffset, nx1 = x1() + xOffset, ny0 = y0() + yOffset, ny1 = y1() + yOffset;

        boolean nx0Fits = nx0 > hitbox.x0() && nx0 < hitbox.x1(), nx1Fits = nx1 > hitbox.x0() && nx1 < hitbox.x1(),
                ny0Fits = ny0 > hitbox.y0() && ny0 < hitbox.y1(), ny1Fits = ny1 > hitbox.y0() && ny1 < hitbox.y1();

       // if ( (nx0Fits || nx1Fits) && (ny0Fits || ny1Fits) ) System.out.printf("%b, %b, %b, %b\n", nx0Fits, nx1Fits, ny0Fits, ny1Fits);

        if ( (nx0Fits || nx1Fits) && (ny0Fits || ny1Fits) ) return true;
        else return collidesInternal(xOffset, yOffset, hitbox);
    }

    public boolean isWithin(float x0, float y0, float x1, float y1)
    {
        boolean nx0Fits = x0() > x0 && x0() < x1, nx1Fits = x1() > x0 && x1() < x1,
                ny0Fits = y0() > y0 && y0() < y1, ny1Fits = y1() > y0 && y1() < y1;

        return ( (nx0Fits || nx1Fits) && (ny0Fits || ny1Fits) );
    }

    private boolean collidesInternal(float xOffset, float yOffset, Hitbox hitbox)
    {
        float nx0 = x0() + xOffset, nx1 = x1() + xOffset, ny0 = y0() + yOffset, ny1 = y1() + yOffset;

        boolean fitsInNx0 = hitbox.x0() > nx0 && hitbox.x0() < nx1, fitsInNx1 = hitbox.x1() > nx0 && hitbox.x1() < nx1,
                fitsInNy0 = hitbox.y0() > ny0 && hitbox.y0() < ny1, fitsInNy1 = hitbox.y1() > ny0 && hitbox.y1() < ny1;

        return (fitsInNx0 || fitsInNx1) && (fitsInNy0 || fitsInNy1);
    }

    public float x0()
    {
        return x0 + pos.x;
    }

    public float x1()
    {
        return x1 + pos.x;
    }

    public float y0()
    {
        return y0 + pos.y;
    }

    public float y1()
    {
        return y1 + pos.y;
    }

    public float mx()
    {
        return mx + pos.x;
    }

    public float my()
    {
        return my + pos.y;
    }
}
