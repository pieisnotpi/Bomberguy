package com.pieisnotpi.bomberguy.physics;

import com.pieisnotpi.engine.scene.GameObject;
import org.joml.Vector2f;

public abstract class DynamicPhysicsObject extends GameObject implements PhysicsObject
{
    protected Hitbox hitbox;
    protected Vector2f velocity = new Vector2f();
    protected Vector2f acceleration = new Vector2f();

    protected int[] collidesWith = null;
    protected float bounciness = 0.5f;
    protected float mass = 1f;
    protected boolean collideable = true;

    protected DynamicPhysicsObject() {}

    protected DynamicPhysicsObject(float x, float y, float hbX0, float hbY0, float hbX1, float hbY1)
    {
        super();

        transform.setTranslate(x, y, 0);
        hitbox = new Hitbox(hbX0, hbY0, hbX1, hbY1, transform.pos);
    }

    public Hitbox getHitbox()
    {
        return hitbox;
    }

    @Override
    public Vector2f getVelocity()
    {
        return velocity;
    }

    @Override
    public Vector2f getAcceleration()
    {
        return acceleration;
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

    public boolean requiresSimulation()
    {
        return velocity.x != 0 || velocity.y != 0;
    }

    public boolean isCollideable()
    {
        return collideable;
    }

    public void setBounciness(float bounciness)
    {
        this.bounciness = bounciness;
    }

    public void setMass(float mass)
    {
        this.mass = mass;
    }

    public void setCollideable(boolean collideable)
    {
        this.collideable = collideable;
    }

    public void applyForce(float x, float y)
    {
        velocity.add(x / mass, y / mass);
    }
}
