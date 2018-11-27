package engine.particle.link;

import engine.particle.Particle;
import engine.collision.ParticleCollision;
import world.Vector3D;

public class ParticleCable extends ParticleLink {

    private float maxLength, restitution;

    public ParticleCable(Particle p1, Particle p2, float maxLength, float restitution) {
        super(p1, p2);
        this.maxLength = maxLength;
        this.restitution = restitution;
    }

    public float getMaxLength() {
        return maxLength;
    }

    public float getRestitution() {
        return restitution;
    }

    public final boolean fillCollision(ParticleCollision collision, int limit) {
        float currentLength = getCurrentLength();
        if (currentLength < maxLength)
            return false;

        collision.setParticles(p1, p2);
        Vector3D normal = new Vector3D(p1.getPosition(), p2.getPosition());
        normal.normalize();
        collision.setNormal(normal);
        collision.setIntersection(currentLength-maxLength);
        collision.setRestitution(restitution);
        return true;
    }
}
