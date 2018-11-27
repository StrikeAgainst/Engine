package engine;

import world.Vector3D;

public class Gravity implements ForceGenerator {

    private Vector3D gravity;

    public Gravity(Vector3D gravity) {
        this.gravity = gravity;
    }

    public void updateForce(RigidBody r, float tick) {
        if (r.hasFiniteMass()) {
            Vector3D force = new Vector3D(gravity);
            force.stretch(r.getMass());
            r.applyForce(force);
        }
    }
}
