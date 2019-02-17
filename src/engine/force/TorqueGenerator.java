package engine.force;

import engine.PhysicalObject;

public interface TorqueGenerator {

    void updateTorque(PhysicalObject o, float tick);
}
