package engine.force;

import engine.PhysicalObject;
import core.Vector3;

public class Gravity implements ForceGenerator {

    private Vector3 gravity;

    public Gravity(Vector3 gravity) {
        this.gravity = gravity;
    }

    public void updateForce(PhysicalObject o, float tick) {
        Vector3 force = new Vector3(gravity);
        force.scale(o.getMass());
        o.applyForce(force);
    }
}
