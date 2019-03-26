package engine.collision.bounding;

import core.Vector3;
import engine.collision.ContactDetector;
import engine.collision.ContactProperties;

import java.util.ArrayList;

public class BoundingHalfSpace extends CollidableBounding {

    protected Vector3 normal;
    protected double offset;

    public BoundingHalfSpace() {
        this(new Vector3(0, 0, 1), 0);
    }

    public BoundingHalfSpace(Vector3 normal, double offset) {
        this.normal = normal.getNormalized();
        this.offset = offset;
    }

    public ArrayList<ContactProperties> contactsWith(CollidableBounding bounding) {
        if (bounding instanceof AssembledBounding)
            return bounding.contactsWith(this);
        else if (bounding instanceof SimpleBounding)
            return ContactDetector.BoundingOnHalfSpace((SimpleBounding) bounding, this);

        return new ArrayList<>();
    }

    public Vector3 getNormal() {
        return normal;
    }

    public double getOffset() {
        return offset;
    }

    public double getXUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    public double getXLowerBound() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getYUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    public double getYLowerBound() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getZUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    public double getZLowerBound() {
        return Double.NEGATIVE_INFINITY;
    }

    public String getAttributesString() {
        return "normal="+normal.toString()+", offset="+offset;
    }
}
