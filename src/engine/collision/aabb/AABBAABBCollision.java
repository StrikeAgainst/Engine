package engine.collision.aabb;

import engine.EngineObject;
import engine.PhysicalObject;
import world.Vector3D;
import world.bounding.Bounding;

public class AABBAABBCollision extends AABBCollision {

    public AABBAABBCollision(PhysicalObject thisNode, EngineObject otherNode, float timeFrame) {
        super(thisNode, otherNode, timeFrame);
    }

    public boolean resolve() {
        /*Vector3D sepVel = calculateSeparatingVelocity();

        float svx = sepVel.getX(), svy = sepVel.getY(), svz = sepVel.getZ();
        float in, x_in, y_in, z_in;
        Bounding thisBounding = thisNode.getBounding(), otherBounding = otherNode.getBounding();

        if (svx > 0 && otherBounding.getBackBound() > thisBounding.getFrontBound())
            x_in = (otherBounding.getBackBound()-thisBounding.getFrontBound())/svx;
        else if (svx < 0 && otherBounding.getFrontBound() < thisBounding.getBackBound())
            x_in = (otherBounding.getFrontBound()-thisBounding.getBackBound())/svx;
        else return false;

        if (svy > 0 && otherBounding.getRightBound() > thisBounding.getLeftBound())
            y_in = (otherBounding.getRightBound()-thisBounding.getLeftBound())/svy;
        else if (svy < 0 && otherBounding.getLeftBound() < thisBounding.getRightBound())
            y_in = (otherBounding.getLeftBound()-thisBounding.getRightBound())/svy;
        else return false;

        if (svz > 0 && otherBounding.getBottomBound() > thisBounding.getTopBound())
            z_in = (otherBounding.getBottomBound()-thisBounding.getTopBound())/svz;
        else if (svz < 0 && otherBounding.getTopBound() < thisBounding.getBottomBound())
            z_in = (otherBounding.getTopBound()-thisBounding.getBottomBound())/svz;
        else return false;

        in = Math.min(Math.min(x_in, y_in), z_in);

        Vector3D movement = new Vector3D(sepVel);
        movement.stretch(in);*/

        return false;
    }

    public String toString() {
        return "Collision:[" + thisNode.toString() + ", " + otherNode.toString() + "]";
    }
}
