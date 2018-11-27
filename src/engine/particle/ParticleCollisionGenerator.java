package engine.particle;

import engine.collision.ParticleCollision;

public interface ParticleCollisionGenerator {

    int addCollision(ParticleCollision collision, int limit);
}
