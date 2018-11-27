package engine.particle.link;

import engine.particle.Particle;
import engine.collision.ParticleCollision;
import world.Vector3D;

public class ParticleRod extends ParticleLink {

    private float length;

    public ParticleRod(Particle p1, Particle p2, float length) {
        super(p1, p2);
        this.length = length;
    }

    public final boolean fillCollision(ParticleCollision collision, int limit) {
        float currentLength = getCurrentLength();
        if (currentLength == length)
            return false;

        collision.setParticles(p1, p2);
        Vector3D normal = new Vector3D(p1.getPosition(), p2.getPosition());
        normal.normalize();
        float intersection;
        if (currentLength > length)
            intersection = currentLength-length;
        else {
            normal.stretch(-1);
            intersection = length-currentLength;
        }
        collision.setNormal(normal);
        collision.setIntersection(intersection);
        collision.setRestitution(0);
        return true;
    }
}
