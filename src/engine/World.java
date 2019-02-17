package engine;

import engine.collision.CollisionHandler;
import engine.force.ForceRegistry;

import java.util.ArrayList;

public class World {

    private ArrayList<RigidObject> objects;
    private ForceRegistry forceRegistry = ForceRegistry.get();
    private CollisionHandler collisionHandler;
    private int maxCollisions, iterations;

    public World(ArrayList<RigidObject> bodies, int maxCollisions) {
        this(bodies, maxCollisions, 0);
    }

    public World(ArrayList<RigidObject> objects, int maxCollisions, int iterations) {
        this.objects = objects;
        this.maxCollisions = maxCollisions;
        collisionHandler = new CollisionHandler(iterations);
    }

    public void startFrame() {
        resetAllForces();
    }

    public void resetAllForces() {
        for (RigidObject o : objects) {
            o.transformInertiaTensor();

            o.clearForce();
            o.clearTorque();
        }
    }

    public void update(float tick) {
        for (RigidObject o : objects)
            o.update(tick);
    }

    public ArrayList<RigidObject> getObjects() {
        return objects;
    }

    public void runPhysics(float tick) {
        forceRegistry.updateForces(tick);
        update(tick);
    }
}
