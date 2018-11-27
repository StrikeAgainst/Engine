package engine.particle;

public interface ParticleForceGenerator {

    void updateForce(Particle p, float tick);
}
