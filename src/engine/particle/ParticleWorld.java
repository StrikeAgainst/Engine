package engine.particle;

import engine.collision.CollisionHandler;
import engine.collision.ParticleCollision;

import java.util.ArrayList;

public class ParticleWorld {

    private ArrayList<Particle> particles;
    private ParticleForceRegistry particleForceRegistry = ParticleForceRegistry.get();
    private CollisionHandler collisionHandler;
    private ArrayList<ParticleCollisionGenerator> particleCollisionGenerators = new ArrayList<>();
    private ArrayList<ParticleCollision> particleCollisions;
    private int maxCollisions, iterations;

    public ParticleWorld(ArrayList<Particle> particles, int maxCollisions) {
        this(particles, maxCollisions, 0);
    }

    public ParticleWorld(ArrayList<Particle> particles, int maxCollisions, int iterations) {
        this.particles = particles;
        this.maxCollisions = maxCollisions;
        collisionHandler = new CollisionHandler(iterations);
    }

    public void startFrame() {
        resetTotalForces();
    }

    public int generateCollisions() {
        int limit = maxCollisions;
        int collision_index = 0;
        for (ParticleCollisionGenerator gen : particleCollisionGenerators) {
            ParticleCollision coll = particleCollisions.get(collision_index);
            int used = gen.addCollision(coll, limit);
            collision_index += used;
            limit -= used;
            if (limit <= 0)
                break;
        }
        return maxCollisions - limit;
    }

    public void resetTotalForces() {
        for (Particle particle : particles)
            particle.clearForce();
    }

    public void update(float tick) {
        for (Particle particle : particles)
            particle.update(tick);
    }

    public ArrayList<Particle> getParticles() {
        return particles;
    }

    public void runPhysics(float tick) {
        particleForceRegistry.updateForces(tick);
        update(tick);
        int usedCollisions = generateCollisions();
        if (iterations == 0)
            collisionHandler.setIterations(usedCollisions*2);
        collisionHandler.resolve(particleCollisions, tick);
    }
}
