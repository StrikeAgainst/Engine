package engine.collision.obb;

import engine.EngineObject;
import engine.PhysicalObject;
import engine.collision.Collision;

public abstract class OBBCollision extends Collision {

    public OBBCollision(PhysicalObject thisNode, EngineObject otherNode, float timeFrame) {
        super(thisNode, otherNode, timeFrame);
    }
}
