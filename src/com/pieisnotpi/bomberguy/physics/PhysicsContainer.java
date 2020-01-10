package com.pieisnotpi.bomberguy.physics;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simulates physics in a very stupidly contained environment
 */
public class PhysicsContainer
{
    private Map<Integer, List<PhysicsObject>> physObjects;

    private int interrimSteps = 4;
    private float interrimStepMult = 1f/interrimSteps;

    public PhysicsContainer()
    {
        physObjects = new HashMap<>();
    }

    public void simulate(float timeStep)
    {
        // Simulate accelerations
        physObjects.forEach( (layer, list) ->
        {
            list.forEach(obj -> obj.getVelocity().add(obj.getAcceleration()));
        });

        // Simulate interactions
        for (int i = 0; i < interrimSteps; i++)
        {
            physObjects.forEach( (layer, list) ->
            {
                list.forEach(obj ->
                {
                    if (obj.requiresSimulation() && obj.isCollideable())
                    {
                        DynamicPhysicsObject dyn = (DynamicPhysicsObject) obj;

                        for (int j : dyn.collidesWith)
                        {
                            if(physObjects.get(j) != null) simulateIndividual(timeStep, dyn, physObjects.get(j));
                        }
                    }
                });
            });

            physObjects.forEach( (layer, list) ->
            {
                list.forEach(obj ->
                {
                    if (obj.requiresSimulation() && obj.isCollideable())
                    {
                        DynamicPhysicsObject dyn = (DynamicPhysicsObject) obj;

                        dyn.getTransform().translate(dyn.velocity.x*timeStep*interrimStepMult, dyn.velocity.y*timeStep*interrimStepMult, 0);
                    }
                });
            });
        }
    }

    private void simulateIndividual(float timeStep, DynamicPhysicsObject primary, List<PhysicsObject> objs)
    {
        Vector2f velocity = primary.getVelocity();
        velocity.add(primary.getAcceleration());

        float xShift = velocity.x*timeStep*interrimStepMult, yShift = velocity.y*timeStep*interrimStepMult;

        objs.forEach(secondary ->
        {
            if (secondary.isCollideable() && primary.collidesWith(xShift, yShift, secondary))
                simulateCollision(primary, secondary, xShift, yShift);
        });
    }

    private void simulateCollision(PhysicsObject primary, PhysicsObject secondary, float xShift, float yShift)
    {
        primary.onCollision(secondary);

        Vector2f primVel = primary.getVelocity(), secVel = secondary.getVelocity();

        Hitbox primBox = primary.getHitbox(), secBox = secondary.getHitbox();

        boolean xCollided = yShift != 0 && primBox.collidesWith(0, yShift, secBox);
        boolean yCollided = xShift != 0 && primBox.collidesWith(xShift, 0, secBox);

        if(!xCollided && !yCollided) xCollided = yCollided = true;

        float primMass = primary.getMass(), secMass = secondary.getMass();
        float bounce = primary.getBounciness()*secondary.getBounciness();

        // SIMULATE X MOVEMENT
        if (xCollided)
        {
            if (secMass == -1) primVel.y = -primVel.y*bounce;
            else
            {
                float refVel = primVel.y - secVel.y;

                primVel.y = ( ( (primMass - secMass) * refVel ) / (primMass + secMass) * bounce ) + secVel.y;
                secVel.y = ( (2 * primMass * refVel) / (primMass + secMass) * bounce) + secVel.y;
            }
        }

        // SIMULATE Y MOVEMENT
        if (yCollided)
        {
            if (secMass == -1) primVel.x = -primVel.x*bounce;
            else
            {
                float refVel = primVel.x - secVel.x;

                primVel.x = ( ( (primMass - secMass) * refVel ) / (primMass + secMass) * bounce ) + secVel.x;
                secVel.x = ( (2 * primMass * refVel) / (primMass + secMass) * bounce) + secVel.x;
            }
        }
    }

    public void setInterrimSteps(int interrimSteps)
    {
        this.interrimSteps = interrimSteps;
        interrimStepMult = 1f/interrimSteps;
    }

    public void addPhysicsObject(int layer, PhysicsObject obj)
    {
        if (!physObjects.containsKey(layer)) physObjects.put(layer, new ArrayList<>());
        physObjects.get(layer).add(obj);
    }

    public void removePhysicsObject(int layer, PhysicsObject obj)
    {
        physObjects.get(layer).remove(obj);
    }

    public void clear()
    {
        physObjects.forEach( (layer, list) -> list.clear());
    }

    public void applyInstantForce(int layer, float x, float y, float radius, float force)
    {
        List<PhysicsObject> objs = physObjects.get(layer);

        objs.forEach(obj ->
        {
            if (obj instanceof DynamicPhysicsObject)
            {
                DynamicPhysicsObject dyn = (DynamicPhysicsObject) obj;
                float dx = dyn.hitbox.mx() - x, dy = dyn.hitbox.my() - y;
                float dif = radius - (float) Math.sqrt(dx*dx + dy*dy);

                if (dif > 0)
                {
                    double angle = Math.atan2(dy, dx);
                    dyn.velocity.add((float) Math.cos(angle) * dif * force, (float) Math.sin(angle) * dif * force);
                }
            }
        });
    }
}
