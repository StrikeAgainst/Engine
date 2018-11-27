package engine.particle;

import world.Vector3D;

public class ParticleGravity implements ParticleForceGenerator {

    private Vector3D gravity;

    public ParticleGravity(Vector3D gravity) {
        this.gravity = gravity;
    }

    public void updateForce(Particle p, float tick) {
        if (p.hasFiniteMass()) {
            Vector3D force = new Vector3D(gravity);
            force.stretch(p.getMass());
            p.applyForce(force);
        }
    }
}
