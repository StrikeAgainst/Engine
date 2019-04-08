package engine;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import core.Matrix3x3;
import core.Point3;
import core.Quaternion;
import core.Vector3;
import engine.collision.bounding.AssembledBounding;
import engine.force.ForceRegistry;

import java.util.ArrayList;

public class AssembledObject extends RigidObject {

    public ArrayList<RigidObject> objects = new ArrayList<>();
    protected Transformation transformation;

    public AssembledObject() {
        super(new Transformation());
        bounding = new AssembledBounding(this, new ArrayList<>());
    }

    public AssembledObject(ArrayList<RigidObject> objects) {
        this();
        add(objects);
    }

    public void render(GL2 gl, GLUT glut) {
        for (RigidObject o : objects)
            o.render(gl, glut);
    }

    public void add(RigidObject object) {
        ArrayList<RigidObject> objects = new ArrayList<>();
        objects.add(object);
        add(objects);
    }

    public void add(ArrayList<RigidObject> objects) {
        for (RigidObject o : objects) {
            ObjectRegistry.get().remove(o);
            ForceRegistry.get().pause(o);

            objects.add(o);
            ((AssembledBounding) bounding).add(o.getBounding());
        }

        updateAssembly();
    }

    public void remove(RigidObject object) {
        ArrayList<RigidObject> objects = new ArrayList<>();
        objects.add(object);
        remove(objects);
    }

    public void remove(ArrayList<RigidObject> objects) {
        for (RigidObject o : objects) {
            objects.remove(o);
            ((AssembledBounding) bounding).remove(o.getBounding());

            ObjectRegistry.get().add(o);
            ForceRegistry.get().resume(o);
        }

        updateAssembly();
    }

    public void updateAssembly() {
        if (objects.isEmpty())
            return;

        for (RigidObject o : objects)
            if (o.getMass() == Double.POSITIVE_INFINITY) {
                inverseMass = 0;
                transformation.reset();
                inertiaTensor = InertiaTensor.createImmovable();
                return;
            }

        double mass = 0;
        Point3 massCenter = new Point3();
        for (RigidObject o : objects) {
            mass += o.getMass();
            massCenter.add(new Vector3(o.getPosition()).scaled(o.getMass()));
        }

        inverseMass = 1/mass;
        massCenter.scale(inverseMass);
        transformation.set(massCenter, new Quaternion());

        Matrix3x3 tensorMatrix = Matrix3x3.getEmpty();
        for (RigidObject o : objects) {
            Vector3 offset = Vector3.offset(o.getPosition(), massCenter);
            tensorMatrix.add(o.getInertiaTensor().offset(offset, o.getMass()));
        }

        inertiaTensor = new InertiaTensor(tensorMatrix);
    }
}
