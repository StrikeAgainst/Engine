package engine.force;

import engine.RigidObject;

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

    public void add(RigidObject o, ForceGenerator gen) {
        registrations.add(new ForceRegistration(o, gen));
    }

    public boolean has(RigidObject o, ForceGenerator gen) {
        for (ForceRegistration reg : registrations)
            if (reg.matches(o, gen))
                return true;

        return false;
    }

    public void remove(RigidObject o, ForceGenerator gen) {
        for (ForceRegistration reg : registrations)
            if (reg.matches(o, gen)) {
                registrations.remove(reg);
                break;
            }
    }

    public void pause(RigidObject o) {
        for (ForceRegistration reg : registrations)
            if (reg.matches(o))
                reg.pause();
    }

    public void resume(RigidObject o) {
        for (ForceRegistration reg : registrations)
            if (reg.matches(o))
                reg.resume();
    }

    public void clear() {
        registrations.clear();
    }

    public void updateForces(double tick) {
        for (ForceRegistration reg : registrations)
            if (!reg.paused())
                reg.getGenerator().updateForce(reg.getObject(), tick);
    }

    public class ForceRegistration {
        private RigidObject o;
        private ForceGenerator gen;
        private boolean paused = false;

        public ForceRegistration(RigidObject o, ForceGenerator gen) {
            this.o = o;
            this.gen = gen;
        }

        public boolean matches(RigidObject o) {
            return (this.o == o);
        }

        public boolean matches(ForceGenerator gen) {
            return (this.gen == gen);
        }

        public boolean matches(RigidObject o, ForceGenerator gen) {
            return (matches(o) && matches(gen));
        }

        public boolean paused() {
            return paused;
        }

        public void pause() {
            paused = true;
        }

        public void resume() {
            paused = false;
        }

        public RigidObject getObject() {
            return o;
        }

        public ForceGenerator getGenerator() {
            return gen;
        }
    }
}
