package engine.force;

import engine.RigidObject;

public interface ForceGenerator {

    void updateForce(RigidObject o, double tick);
}
