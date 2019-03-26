package engine.collision;

import core.Point3;
import core.Vector3;

public class ContactProperties {

    public Point3 point;
    public Vector3 normal;
    public double depth;

    public ContactProperties(Point3 point, Vector3 normal, double depth) {
        this.point = point;
        this.normal = normal.getNormalized();
        this.depth = depth;
    }

    public double getDepth() {
        return depth;
    }

    public Point3 getPoint() {
        return point;
    }

    public Vector3 getNormal() {
        return normal;
    }
}
