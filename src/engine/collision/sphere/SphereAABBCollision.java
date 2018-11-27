package engine.collision.sphere;

import engine.EngineObject;
import engine.PhysicalObject;

public class SphereAABBCollision extends SphereCollision {

    public SphereAABBCollision(PhysicalObject thisNode, EngineObject otherNode, float timeFrame) {
        super(thisNode, otherNode, timeFrame);
    }

    public boolean resolve() {
        return false;
    }
}
