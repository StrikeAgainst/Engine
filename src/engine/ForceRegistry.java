package engine;

import java.util.ArrayList;

public class ForceRegistry {

    private static ForceRegistry registry = null;
    private ArrayList<ForceRegistration> registrations = new ArrayList<>();

    private ForceRegistry() {}

    public static ForceRegistry get() {
        if (registry == null)
            registry = new ForceRegistry();
        return registry;
    }

    public void add(RigidBody b, ForceGenerator gen) {
        registrations.add(new ForceRegistration(b, gen));
    }

    public void remove(RigidBody b, ForceGenerator gen) {
        for (ForceRegistration reg : registrations) {
            if (reg.matches(b, gen)) {
                registrations.remove(reg);
                break;
            }
        }
    }

    public void clear() {
        registrations.clear();
    }

    public void updateForces(float duration) {
        for (ForceRegistration reg : registrations) {
            reg.getGenerator().updateForce(reg.getBody(), duration);
        }
    }

    public class ForceRegistration {
        private RigidBody b;
        private ForceGenerator gen;

        public ForceRegistration(RigidBody b, ForceGenerator gen) {
            this.b = b;
            this.gen = gen;
        }

        public boolean matches(RigidBody b, ForceGenerator gen) {
            return (this.b == b && this.gen == gen);
        }

        public RigidBody getBody() {
            return b;
        }

        public ForceGenerator getGenerator() {
            return gen;
        }
    }
}
