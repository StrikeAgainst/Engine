package engine.collision;

import core.Point3;
import core.Vector3;
import engine.collision.bounding.PrimitiveBounding;

public class PrimitiveContact extends Contact {

    protected Point3 contactPoint;
    protected Vector3 normal;
    protected float depth;

    public PrimitiveContact(PrimitiveBounding bounding1, PrimitiveBounding bounding2, Point3 contactPoint, Vector3 normal, float depth) {
        super(bounding1, bounding2);
        this.contactPoint = contactPoint;
        this.normal = normal;
        this.depth = depth;
    }

    public float getDepth() {
        return depth;
    }

    public String toString() {
        String cp = (contactPoint == null?"null":contactPoint.toString());
        String n = (normal == null?"null":normal.toString());
        return "PrimitiveContact:[bounding1: "+bounding1.getNameString()+", bounding2: "+bounding2.getNameString()+", ContactPoint:"+cp+", normal:"+n+", depth:"+depth+"]";
    }
}
