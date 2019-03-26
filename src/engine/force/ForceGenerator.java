package engine.force;

import engine.PhysicalObject;

public interface ForceGenerator {

    void updateForce(PhysicalObject o, double tick);
}
