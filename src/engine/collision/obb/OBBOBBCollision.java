package engine.collision.obb;

import engine.EngineObject;
import engine.PhysicalObject;

public class OBBOBBCollision extends OBBCollision {

    public OBBOBBCollision(PhysicalObject thisNode, EngineObject otherNode, float timeFrame) {
        super(thisNode, otherNode, timeFrame);
    }

    public boolean resolve() {
        return false;
    }
}
