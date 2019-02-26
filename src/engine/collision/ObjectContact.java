package engine.collision;

import core.*;
import engine.Collidable;
import engine.Material;
import engine.PhysicalObject;
import engine.RigidObject;
import engine.collision.bounding.CollidableBounding;

public class ObjectContact extends Contact {

    public static final float CLOSING_VELOCITY_MIN_LIMIT = 0.1f; //todo: adjust

    protected PhysicalObject[] objects = new PhysicalObject[2];
    protected Vector3[] offsets = new Vector3[2], velocities = new Vector3[2], accVelocities = new Vector3[2];
    protected Material material1, material2;

    protected Point3 point;
    protected Vector3 normal, closingVelocity, closingAccVelocity;
    protected float depth, friction, restitution, deltaVelocity;
    protected Matrix3x3 toLocal, toGlobal;

    private ObjectContact(CollidableBounding bounding, CollidableBounding otherBounding, ContactProperties properties) {
        super(bounding, otherBounding);
        this.point = properties.point;
        this.normal = properties.normal;
        this.depth = properties.depth;
    }

    public ObjectContact(PhysicalObject object, Collidable collidable, ContactProperties properties) {
        this(object.getBounding(), collidable.getBounding(), properties);
        material1 = object.getMaterial();
        material2 = collidable.getMaterial();

        if (collidable instanceof PhysicalObject)
            this.objects = new PhysicalObject[] {object, (PhysicalObject) collidable};
        else
            this.objects = new PhysicalObject[] {object};
    }

    public PhysicalObject[] getObjects() {
        return objects;
    }

    public Vector3[] getOffsets() {
        return offsets;
    }

    public Vector3 getNormal() {
        return normal;
    }

    public float getDepth() {
        return depth;
    }

    public float getClosingVelocityOnNormal() {
        return closingVelocity.getX();
    }

    public Vector3 getClosingVelocity() {
        return closingVelocity;
    }

    public Vector3 getClosingAccVelocity() {
        return closingAccVelocity;
    }

    public float getDeltaVelocity() {
        return deltaVelocity;
    }

    public Matrix3x3 getToGlobal() {
        return toGlobal;
    }

    public Matrix3x3 getToLocal() {
        return toLocal;
    }

    public Vector3 toGlobal(Vector3 v) {
        return toGlobal.product(v);
    }

    public Vector3 toLocal(Vector3 v) {
        return toLocal.product(v);
    }

    public float getFriction() {
        return friction;
    }

    public float getRestitution() {
        return restitution;
    }

    public boolean involves(RigidObject object) {
        return (this.objects[0] == object || this.objects[1] == object);
    }

    public void updateDepth(PhysicalObject[] objects, Vector3[] linearChange, Vector3[] angularChange) {
        int[] sign = new int[] {-1, 1};
        for (int i = 0; i < this.objects.length; i++)
            for (int j = 0; j < objects.length; j++) {
                if (this.objects[i] != objects[j])
                    continue;

                Vector3 deltaPoint = Vector3.sum(linearChange[j], Vector3.cross(angularChange[j], offsets[i]));
                depth += Vector3.dot(deltaPoint, normal)*sign[i];
            }
    }

    public void updateClosingVelocity(PhysicalObject[] objects, Vector3[] linearChange, Vector3[] angularChange) {
        int[] sign = new int[] {1, -1};
        for (int i = 0; i < this.objects.length; i++)
            for (int j = 0; j < objects.length; j++) {
                if (this.objects[i] != objects[j])
                    continue;

                Vector3 deltaVelocity = Vector3.sum(linearChange[j], Vector3.cross(angularChange[j], offsets[i]));
                closingVelocity.add(toLocal(deltaVelocity).scaled(sign[i]));
                calcDeltaVelocity();
            }
    }

    public void calcInternals(float tick) {
        friction = Material.getFriction(material1, material2);
        restitution = 0.1f;//Material.getRestitution(material1, material2);

        toGlobal = ONB.getFromXAxis(normal).toMatrix();
        toLocal = toGlobal.getTransposed();

        closingVelocity = new Vector3();
        closingAccVelocity = new Vector3();
        int[] sign = new int[] {1, -1};

        for (int i = 0; i < objects.length; i++) {
            offsets[i] = Vector3.offset(objects[i].getPosition(), point);

            accVelocities[i] = toLocal(objects[i].getLastAcceleration().scaled(tick));
            velocities[i] = Vector3.sum(toLocal(Vector3.sum(Vector3.cross(objects[i].getRotation(), offsets[i]), objects[i].getVelocity())), new Vector3(0, accVelocities[i].getY(), accVelocities[i].getZ()));

            closingVelocity.add(velocities[i].scaled(sign[i]));
            closingAccVelocity.add(accVelocities[i].scaled(sign[i]));
        }
        calcDeltaVelocity();
    }

    public void calcDeltaVelocity() {
        deltaVelocity = -closingVelocity.getX();

        if (deltaVelocity >= CLOSING_VELOCITY_MIN_LIMIT)
            deltaVelocity -= restitution * (closingVelocity.getX() - Vector3.dot(closingAccVelocity, normal));
    }

    public String toString() {
        StringBuilder obj = new StringBuilder();
        for (int i = 0; i < objects.length; i++)
            obj.append("object"+i+": "+objects[i].getNameIDString()+", ");

        String cp = (point == null?"null":point.toString());
        String n = (normal == null?"null":normal.toString());

        return "ObjectContact:["+obj+"ContactPoint:"+cp+", normal:"+n+", depth:"+depth+"]";
    }
}
