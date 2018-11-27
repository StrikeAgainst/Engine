package engine.particle;

import java.util.ArrayList;

public class ParticleForceRegistry {

    private static ParticleForceRegistry registry = null;
    private ArrayList<ParticleForceRegistration> registrations = new ArrayList<>();

    private ParticleForceRegistry() {}

    public static ParticleForceRegistry get() {
        if (registry == null)
            registry = new ParticleForceRegistry();
        return registry;
    }

    public void add(Particle p, ParticleForceGenerator gen) {
        registrations.add(new ParticleForceRegistration(p, gen));
    }

    public void remove(Particle p, ParticleForceGenerator gen) {
        for (ParticleForceRegistration reg : registrations) {
            if (reg.matches(p, gen)) {
                registrations.remove(reg);
                break;
            }
        }
    }

    public void clear() {
        registrations.clear();
    }

    public void updateForces(float duration) {
        for (ParticleForceRegistration reg : registrations) {
            reg.getGenerator().updateForce(reg.getParticle(), duration);
        }
    }

    public class ParticleForceRegistration {
        private Particle p;
        private ParticleForceGenerator gen;

        public ParticleForceRegistration(Particle p, ParticleForceGenerator gen) {
            this.p = p;
            this.gen = gen;
        }

        public boolean matches(Particle p, ParticleForceGenerator gen) {
            return (this.p == p && this.gen == gen);
        }

        public Particle getParticle() {
            return p;
        }

        public ParticleForceGenerator getGenerator() {
            return gen;
        }
    }
}
