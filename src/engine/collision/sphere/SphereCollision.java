package engine.collision.sphere;

import engine.EngineObject;
import engine.PhysicalObject;
import engine.collision.Collision;

public abstract class SphereCollision extends Collision {

    public SphereCollision(PhysicalObject thisNode, EngineObject otherNode, float timeFrame) {
        super(thisNode, otherNode, timeFrame);
    }
}
