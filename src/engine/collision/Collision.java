package engine.collision;

import engine.EngineObject;
import engine.PhysicalObject;
import world.Vector3D;

public abstract class Collision {

    protected PhysicalObject thisNode;
    protected EngineObject otherNode;
    protected Vector3D normal;
    protected float timeFrame, restitution;

    public Collision(PhysicalObject thisNode, EngineObject otherNode, float timeFrame) {
        this.thisNode = thisNode;
        this.otherNode = otherNode;
        this.timeFrame = timeFrame;
    }

    public PhysicalObject getThisNode() {
        return thisNode;
    }

    public EngineObject getOtherNode() {
        return otherNode;
    }

    public float getTimeFrame() {
        return timeFrame;
    }

    public abstract boolean resolve();

    public String toString() {
        return "Collision:[" + thisNode.toString() + ", " + otherNode.toString() + "]";
    }
}
