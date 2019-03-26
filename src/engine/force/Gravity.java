package engine.force;

import engine.PhysicalObject;
import core.Vector3;
import engine.Physics;

public class Gravity implements ForceGenerator {

    private Vector3 gravity;

    public Gravity() {
        this.gravity = Physics.gravity;
    }

    public Gravity(Vector3 gravity) {
        this.gravity = gravity;
    }

    public void updateForce(PhysicalObject o, double tick) {
        o.applyForce(gravity.scaled(o.getMass()));
    }

    public Vector3 getGravity() {
        return gravity;
    }
}
