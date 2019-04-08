package engine.collision.bounding;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import core.Point3;
import core.Vector3;
import engine.RigidObject;
import engine.collision.ContactProperties;

import java.util.ArrayList;

public class AssembledBounding extends ObjectBounding {

    private ArrayList<ObjectBounding> boundings;

    public AssembledBounding(RigidObject object, ArrayList<ObjectBounding> boundings) {
        super(object);
        this.boundings = boundings;
    }

    public void renderSub(GL2 gl, GLUT glut) {
        for (ObjectBounding b : boundings)
            b.renderSub(gl, glut);
    }

    public void add(ObjectBounding bounding) {
        boundings.add(bounding);
    }

    public void remove(ObjectBounding bounding) {
        boundings.remove(bounding);
    }

    public ArrayList<ContactProperties> contactsWith(CollidableBounding bounding) {
        ArrayList<ContactProperties> contacts = new ArrayList<>();
        for (ObjectBounding b : boundings)
            contacts.addAll(bounding.contactsWith(b));

        return contacts;
    }

    public ArrayList<Point3> contactsWith(Point3 origin, Vector3 line) {
        ArrayList<Point3> points = new ArrayList<>();
        for (ObjectBounding b : boundings)
            points.addAll(b.contactsWith(origin, line));

        return points;
    }

    public boolean comprises(Bounding bounding) {
        for (ObjectBounding b : boundings)
            if (b.comprises(bounding))
                return true;

        return super.comprises(bounding);
    }

    public void update() {
        for (ObjectBounding b : boundings)
            b.update();
    }

    public ArrayList<ObjectBounding> getBoundings() {
        return boundings;
    }

    public double getXUpperBound() {
        double max = Double.NEGATIVE_INFINITY;
        for (ObjectBounding b : boundings) {
            double bound = b.getXUpperBound();
            if (max < bound)
                max = bound;
        }

        return max;
    }

    public double getXLowerBound() {
        double min = Double.POSITIVE_INFINITY;
        for (ObjectBounding b : boundings) {
            double bound = b.getXLowerBound();
            if (min > bound)
                min = bound;
        }

        return min;
    }

    public double getYUpperBound() {
        double max = Double.NEGATIVE_INFINITY;
        for (ObjectBounding b : boundings) {
            double bound = b.getYUpperBound();
            if (max < bound)
                max = bound;
        }

        return max;
    }

    public double getYLowerBound() {
        double min = Double.POSITIVE_INFINITY;
        for (ObjectBounding b : boundings) {
            double bound = b.getYLowerBound();
            if (min > bound)
                min = bound;
        }

        return min;
    }

    public double getZUpperBound() {
        double max = Double.NEGATIVE_INFINITY;
        for (ObjectBounding b : boundings) {
            double bound = b.getZUpperBound();
            if (max < bound)
                max = bound;
        }

        return max;
    }

    public double getZLowerBound() {
        double min = Double.POSITIVE_INFINITY;
        for (ObjectBounding b : boundings) {
            double bound = b.getZLowerBound();
            if (min > bound)
                min = bound;
        }

        return min;
    }
}
