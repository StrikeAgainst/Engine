package engine.collision.aabb;

import engine.EngineObject;
import engine.PhysicalObject;

public class AABBSphereCollision extends AABBCollision{

    public AABBSphereCollision(PhysicalObject thisNode, EngineObject otherNode, float timeFrame) {
        super(thisNode, otherNode, timeFrame);
    }

    public boolean resolve() {
        return false;
    }
}
