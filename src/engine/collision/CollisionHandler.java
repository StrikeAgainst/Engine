package engine.collision;

import java.util.ArrayList;

public class CollisionHandler {

    private int iterations;

    public CollisionHandler(int iterations) {
        this.iterations = iterations;
    }

    public void resolve(ArrayList<ParticleCollision> collisions, float tick) {
        int i = 0;
        while (i++ < iterations) {
            float maxSepVel = 0;
            ParticleCollision maxCollision = collisions.get(0);
            for (ParticleCollision c : collisions) {
                float sepVel = c.calculateSeparatingVelocity();
                if (sepVel < maxSepVel) {
                    maxSepVel = sepVel;
                    maxCollision = c;
                }
            }
            maxCollision.resolve(tick);
        }
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
}
