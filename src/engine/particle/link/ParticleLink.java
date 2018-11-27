package engine.particle.link;

import engine.particle.Particle;
import engine.collision.ParticleCollision;

public abstract class ParticleLink {

    protected Particle p1, p2;

    public ParticleLink(Particle p1, Particle p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Particle[] getParticles() {
        return new Particle[]{p1, p2};
    }

    protected final float getCurrentLength() {
        return p1.getPosition().getEuclideanDistance(p2.getPosition());
    }

    public abstract boolean fillCollision(ParticleCollision collision, int limit);
}
