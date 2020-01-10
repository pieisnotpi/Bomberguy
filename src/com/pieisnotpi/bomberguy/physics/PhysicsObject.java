package com.pieisnotpi.bomberguy.physics;

import org.joml.Vector2f;

public interface PhysicsObject
{
    Hitbox getHitbox();
    Vector2f getVelocity();
    Vector2f getAcceleration();

    float getBounciness();
    float getMass();

    boolean requiresSimulation();
    boolean isCollideable();

    default boolean collidesWith(float xShift, float yShift, PhysicsObject secondary)
    {
        return getHitbox().collidesWith(xShift, yShift, secondary.getHitbox());
    }

    default void onCollision(PhysicsObject collidedWith) {}
}
