package engine.force;

import engine.PhysicalObject;

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

    public void add(PhysicalObject o, ForceGenerator gen) {
        registrations.add(new ForceRegistration(o, gen));
    }

    public void remove(PhysicalObject o, ForceGenerator gen) {
        for (ForceRegistration reg : registrations) {
            if (reg.matches(o, gen)) {
                registrations.remove(reg);
                break;
            }
        }
    }

    public void clear() {
        registrations.clear();
    }

    public void updateForces(double tick) {
        for (ForceRegistration reg : registrations) {
            reg.getGenerator().updateForce(reg.getObject(), tick);
        }
    }

    public class ForceRegistration {
        private PhysicalObject o;
        private ForceGenerator gen;

        public ForceRegistration(PhysicalObject o, ForceGenerator gen) {
            this.o = o;
            this.gen = gen;
        }

        public boolean matches(PhysicalObject o, ForceGenerator gen) {
            return (this.o == o && this.gen == gen);
        }

        public PhysicalObject getObject() {
            return o;
        }

        public ForceGenerator getGenerator() {
            return gen;
        }
    }
}
