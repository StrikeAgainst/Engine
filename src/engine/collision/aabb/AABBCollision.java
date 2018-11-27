package engine.collision.aabb;

import engine.EngineObject;
import engine.PhysicalObject;
import engine.collision.Collision;

public abstract class AABBCollision extends Collision {

    public AABBCollision(PhysicalObject thisNode, EngineObject otherNode, float timeFrame) {
        super(thisNode, otherNode, timeFrame);
    }
}
