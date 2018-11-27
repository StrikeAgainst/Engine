package engine.collision;

import engine.particle.Particle;
import world.Vector3D;

public class ParticleCollision {

    private Particle p1, p2 = null;
    private float restitution;
    private float intersection;
    private Vector3D normal;

    public ParticleCollision(Particle p1, Particle p2) throws Exception {
        if (p1 == null) {
            if (p2 == null)
                throw new Exception();

            this.p1 = p2;
        } else {
            this.p1 = p1;
            this.p2 = p2;
        }
    }

    public Particle[] getParticles() {
        return new Particle[]{p1, p2};
    }

    public void setParticles(Particle p1, Particle p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public float getIntersection() {
        return intersection;
    }

    public void setIntersection(float intersection) {
        this.intersection = intersection;
    }

    public Vector3D getNormal() {
        return normal;
    }

    public void setNormal(Vector3D normal) {
        this.normal = normal;
    }

    protected void resolve(float tick) {
        resolveVelocity(tick);
        resolveIntersection(tick);
    }

    protected float calculateSeparatingVelocity() {
        Vector3D sepVel = p1.getVelocity();
        if (p2 != null)
            sepVel.subtract(p2.getVelocity());
        return Vector3D.dot(sepVel, normal);
    }

    private boolean resolveVelocity(float tick) {
        float sepVel = calculateSeparatingVelocity();
        if (sepVel > 0)
            return false;
        float totalInverseMass = p1.getInverseMass();
        if (p2 != null)
            totalInverseMass += p2.getInverseMass();
        if (totalInverseMass <= 0)
            return false;
        float newSepVel = sepVel*(-1)*restitution;
        Vector3D accVel = p1.getAcceleration();
        if (p2 != null)
            accVel.subtract(p2.getAcceleration());
        float accSepVel = Vector3D.dot(accVel, normal)*tick;
        if (accSepVel < 0) {
            newSepVel += accSepVel*restitution;
            if (newSepVel < 0)
                newSepVel = 0;
        }
        float deltaVel = newSepVel-sepVel;
        float impulse = deltaVel/totalInverseMass;
        Vector3D impulsePerIMass = new Vector3D(normal, impulse);
        p1.setVelocity(Vector3D.sum(p1.getVelocity(), new Vector3D(impulsePerIMass, p1.getInverseMass())));
        if (p2 != null)
            p2.setVelocity(Vector3D.sum(p2.getVelocity(), new Vector3D(impulsePerIMass, p2.getInverseMass())));
        return true;
    }

    private boolean resolveIntersection(float tick) {
        if (intersection <= 0)
            return false;
        float totalInverseMass = p1.getInverseMass();
        if (p2 != null)
            totalInverseMass += p2.getInverseMass();
        if (totalInverseMass <= 0)
            return false;
        Vector3D movePerIMass = new Vector3D(normal, intersection*(-1)/totalInverseMass);
        p1.getPosition().move(new Vector3D(movePerIMass, p1.getInverseMass()));
        if (p2 != null)
            p2.getPosition().move(new Vector3D(movePerIMass, p2.getInverseMass()));
        return true;
    }
}
