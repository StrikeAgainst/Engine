package engine.collision.obb;

import engine.EngineObject;
import engine.PhysicalObject;

public class OBBAABBCollision extends OBBCollision {

    public OBBAABBCollision(PhysicalObject thisNode, EngineObject otherNode, float timeFrame) {
        super(thisNode, otherNode, timeFrame);
    }

    public boolean resolve() {
        return false;
    }
}
