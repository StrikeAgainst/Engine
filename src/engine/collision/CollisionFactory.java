package engine.collision;

import engine.EngineObject;
import engine.PhysicalObject;
import engine.collision.aabb.*;
import engine.collision.obb.*;
import engine.collision.sphere.*;

public class CollisionFactory {

    public static Collision createCollision(PhysicalObject thisNode, EngineObject otherNode, float timeFrame) throws CollisionFactoryException {
        switch (thisNode.getBounding().getType()) {
            case "AABB": {
                switch (otherNode.getBounding().getType()) {
                    case "AABB": {
                        return new AABBAABBCollision(thisNode, otherNode, timeFrame);
                    }
                    case "OBB": {
                        return new AABBOBBCollision(thisNode, otherNode, timeFrame);
                    }
                    case "Sphere": {
                        return new AABBSphereCollision(thisNode, otherNode, timeFrame);
                    }
                    default: {
                        throw new CollisionFactoryException("Unknown bounding type: "+otherNode.getBounding().getType());
                    }
                }
            }
            case "OBB": {
                switch (otherNode.getBounding().getType()) {
                    case "AABB": {
                        return new OBBAABBCollision(thisNode, otherNode, timeFrame);
                    }
                    case "OBB": {
                        return new OBBOBBCollision(thisNode, otherNode, timeFrame);
                    }
                    case "Sphere": {
                        return new OBBSphereCollision(thisNode, otherNode, timeFrame);
                    }
                    default: {
                        throw new CollisionFactoryException("Unknown bounding type: "+otherNode.getBounding().getType());
                    }
                }
            }
            case "Sphere": {
                switch (otherNode.getBounding().getType()) {
                    case "AABB": {
                        return new SphereAABBCollision(thisNode, otherNode, timeFrame);
                    }
                    case "OBB": {
                        return new SphereOBBCollision(thisNode, otherNode, timeFrame);
                    }
                    case "Sphere": {
                        return new SphereSphereCollision(thisNode, otherNode, timeFrame);
                    }
                    default: {
                        throw new CollisionFactoryException("Unknown bounding type: "+otherNode.getBounding().getType());
                    }
                }
            }
            default: {
                throw new CollisionFactoryException("Unknown bounding type: "+thisNode.getBounding().getType());
            }
        }
    }
}
