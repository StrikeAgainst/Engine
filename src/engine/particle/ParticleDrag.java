package engine.particle;

import world.Vector3D;

public class ParticleDrag implements ParticleForceGenerator {

    private float k1, k2;

    public ParticleDrag(float k1, float k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    public void updateForce(Particle p, float duration) {
        Vector3D force = new Vector3D(p.getVelocity());
        float drag_coeff = force.getEuclideanLength();
        drag_coeff = k1*drag_coeff + k2*drag_coeff*drag_coeff;
        force.normalize();
        force.stretch(drag_coeff*(-1));
        p.applyForce(force);
    }
}
