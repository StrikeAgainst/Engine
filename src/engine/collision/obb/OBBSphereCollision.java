package engine.collision.obb;

import engine.EngineObject;
import engine.PhysicalObject;

public class OBBSphereCollision extends OBBCollision {

    public OBBSphereCollision(PhysicalObject thisNode, EngineObject otherNode, float timeFrame) {
        super(thisNode, otherNode, timeFrame);
    }

    public boolean resolve() {
        return false;
    }
}
