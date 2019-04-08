package engine.collision;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import core.*;
import engine.Collidable;
import engine.Material;
import engine.RigidObject;
import engine.collision.bounding.CollidableBounding;

public class ObjectContact extends Contact {

    public static boolean VISIBLE = false;
    public static final double CLOSING_VELOCITY_MIN_LIMIT = 0.1; //todo: adjust

    protected RigidObject[] objects;
    protected Vector3[] offsets = new Vector3[2], velocities = new Vector3[2], accVelocities = new Vector3[2];
    protected Material material1, material2;

    protected Point3 point;
    protected Vector3 normal, closingVelocity, closingAccVelocity;
    protected double depth, friction, restitution, deltaVelocity;
    protected Matrix3x3 toLocal, toGlobal;

    private ObjectContact(CollidableBounding bounding, CollidableBounding otherBounding, ContactProperties properties) {
        super(bounding, otherBounding);
        this.point = properties.point;
        this.normal = properties.normal;
        this.depth = properties.depth;
    }

    public ObjectContact(Collidable collidable1, Collidable collidable2, ContactProperties properties) {
        this(collidable1.getBounding(), collidable2.getBounding(), properties);
        material1 = collidable1.getMaterial();
        material2 = collidable2.getMaterial();
        this.objects = new RigidObject[] {
                ((collidable1 instanceof RigidObject && !collidable1.immovable()) ? (RigidObject) collidable1 : null),
                ((collidable2 instanceof RigidObject && !collidable2.immovable()) ? (RigidObject) collidable2 : null)
        };
    }

    public void render(GL2 gl, GLUT glut) {
        if (VISIBLE) {
            gl.glTranslated(point.getX(), point.getY(), point.getZ());
            gl.glColor3d(1,1,0);
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
            glut.glutSolidSphere(0.1, 12, 4);
            gl.glTranslated(-point.getX(), -point.getY(), -point.getZ());

            Point3 offset = point.offset(normal);
            gl.glBegin (GL2.GL_LINES);
            gl.glVertex3d(point.getX(), point.getY(), point.getZ());
            gl.glVertex3d(offset.getX(), offset.getY(), offset.getZ());
            gl.glEnd();
        }
    }

    public RigidObject[] getObjects() {
        return objects;
    }

    public Vector3[] getOffsets() {
        return offsets;
    }

    public Vector3 getNormal() {
        return normal;
    }

    public double getDepth() {
        return depth;
    }

    public double getClosingVelocityOnNormal() {
        return closingVelocity.getX();
    }

    public Vector3 getClosingVelocity() {
        return closingVelocity;
    }

    public Vector3 getClosingAccVelocity() {
        return closingAccVelocity;
    }

    public double getDeltaVelocity() {
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

    public double getFriction() {
        return friction;
    }

    public double getRestitution() {
        return restitution;
    }

    public boolean involves(RigidObject object) {
        return (this.objects[0] == object || this.objects[1] == object);
    }

    public void updateDepth(RigidObject[] objects, Vector3[] linearChange, Vector3[] angularChange) {
        int[] sign = new int[] {-1, 1};
        for (int i = 0; i < this.objects.length; i++)
            for (int j = 0; j < objects.length; j++) {
                if (this.objects[i] != objects[j] || this.objects[i] == null)
                    continue;

                Vector3 deltaPoint = Vector3.sum(linearChange[j], Vector3.cross(angularChange[j], offsets[i]));
                depth += Vector3.dot(deltaPoint, normal)*sign[i];
                if (ContactResolver.DEBUG_INTERPENETRATIONS)
                    System.out.println("depth updated "+toString());
            }
    }

    public void updateClosingVelocity(RigidObject[] objects, Vector3[] linearChange, Vector3[] angularChange) {
        int[] sign = new int[] {1, -1};
        for (int i = 0; i < this.objects.length; i++)
            for (int j = 0; j < objects.length; j++) {
                if (this.objects[i] != objects[j] || this.objects[i] == null)
                    continue;

                Vector3 deltaVelocity = Vector3.sum(linearChange[j], Vector3.cross(angularChange[j], offsets[i]));
                closingVelocity.add(toLocal(deltaVelocity).scaled(sign[i]));
                calcDeltaVelocity();
                if (ContactResolver.DEBUG_VELOCITYCHANGE)
                    System.out.println("closingVelocity updated "+toString());
            }
    }

    public void calcInternals(double tick) {
        friction = Material.getFriction(material1, material2);
        restitution = Material.getRestitution(material1, material2);

        toGlobal = ONB.getFromXAxis(normal).toMatrix();
        toLocal = toGlobal.getTransposed();

        closingVelocity = new Vector3();
        closingAccVelocity = new Vector3();
        int[] sign = new int[] {1, -1};

        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null)
                continue;

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
            obj.append("object" + i + ": " + (objects[i] == null ? "null" : objects[i].getNameIDString()) + ", ");

        String cp = (point == null?"null":point.toString());
        String n = (normal == null?"null":normal.toString());

        return "ObjectContact:["+obj+"point:"+cp+", normal:"+n+", depth:"+depth+", closingVelocity:"+closingVelocity+"]";
    }
}
