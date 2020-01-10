package com.pieisnotpi.bomberguy.physics;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class StaticPhysicsObject implements PhysicsObject
{
    private static final Vector2f EMPTY = new Vector2f(0);

    protected Hitbox hitbox;
    protected float bounciness = 1f;
    protected float mass = -1f;

    public StaticPhysicsObject(float hx0, float hy0, float hx1, float hy1)
    {
        hitbox = new Hitbox(hx0, hy0, hx1, hy1, new Vector3f(0));
    }

    public StaticPhysicsObject(float hx0, float hy0, float hx1, float hy1, float bounciness)
    {
        hitbox = new Hitbox(hx0, hy0, hx1, hy1, new Vector3f(0));
        this.bounciness = bounciness;
    }

    public Hitbox getHitbox()
    {
        return hitbox;
    }

    @Override
    public Vector2f getVelocity()
    {
        return EMPTY;
    }

    @Override
    public Vector2f getAcceleration()
    {
        return EMPTY;
    }

    @Override
    public float getBounciness()
    {
        return bounciness;
    }

    @Override
    public float getMass()
    {
        return mass;
    }

    @Override
    public boolean requiresSimulation()
    {
        return false;
    }

    @Override
    public boolean isCollideable()
    {
        return true;
    }
}
