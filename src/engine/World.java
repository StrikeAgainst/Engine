package engine;

import engine.collision.CollisionHandler;

import java.util.ArrayList;

public class World {

    private ArrayList<RigidBody> bodies;
    private ForceRegistry forceRegistry = ForceRegistry.get();
    private CollisionHandler collisionHandler;
    private int maxCollisions, iterations;

    public World(ArrayList<RigidBody> bodies, int maxCollisions) {
        this(bodies, maxCollisions, 0);
    }

    public World(ArrayList<RigidBody> bodies, int maxCollisions, int iterations) {
        this.bodies = bodies;
        this.maxCollisions = maxCollisions;
        collisionHandler = new CollisionHandler(iterations);
    }

    public void startFrame() {
        resetAllForces();
    }

    public void resetAllForces() {
        for (RigidBody body : bodies) {
            body.clearForce();
            body.clearTorque();

            body.calcTransformation();
            body.transformInertiaTensor();
        }
    }

    public void update(float tick) {
        for (RigidBody body : bodies)
            body.update(tick);
    }

    public ArrayList<RigidBody> getBodies() {
        return bodies;
    }

    public void runPhysics(float tick) {
        forceRegistry.updateForces(tick);
        update(tick);
    }
}
