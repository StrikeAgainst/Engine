package engine.collision.bounding;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import engine.RigidObject;
import engine.collision.ContactProperties;

import java.util.ArrayList;

public class AssembledBounding extends ObjectBounding {

    private ArrayList<SimpleBounding> boundings;

    public AssembledBounding(RigidObject object, ArrayList<SimpleBounding> boundings) {
        super(object);
        this.boundings = boundings;
    }

    public void renderSub(GL2 gl, GLUT glut) {
        for (SimpleBounding b : boundings)
            b.renderSub(gl, glut);
    }

    public ArrayList<ContactProperties> contactsWith(CollidableBounding bounding) {
        ArrayList<ContactProperties> contacts = new ArrayList<>();
        for (SimpleBounding b : boundings)
            contacts.addAll(bounding.contactsWith(b));

        return contacts;
    }

    public boolean comprises(Bounding bounding) {
        for (SimpleBounding b : boundings)
            if (b.comprises(bounding))
                return true;

        return super.comprises(bounding);
    }

    public void update() {
        for (SimpleBounding b : boundings)
            b.update();
    }

    public ArrayList<SimpleBounding> getBoundings() {
        return boundings;
    }

    public float getXUpperBound() {
        float max = Float.NEGATIVE_INFINITY;
        for (SimpleBounding b : boundings) {
            float bound = b.getXUpperBound();
            if (max < bound)
                max = bound;
        }

        return max;
    }

    public float getXLowerBound() {
        float min = Float.POSITIVE_INFINITY;
        for (SimpleBounding b : boundings) {
            float bound = b.getXLowerBound();
            if (min > bound)
                min = bound;
        }

        return min;
    }

    public float getYUpperBound() {
        float max = Float.NEGATIVE_INFINITY;
        for (SimpleBounding b : boundings) {
            float bound = b.getYUpperBound();
            if (max < bound)
                max = bound;
        }

        return max;
    }

    public float getYLowerBound() {
        float min = Float.POSITIVE_INFINITY;
        for (SimpleBounding b : boundings) {
            float bound = b.getYLowerBound();
            if (min > bound)
                min = bound;
        }

        return min;
    }

    public float getZUpperBound() {
        float max = Float.NEGATIVE_INFINITY;
        for (SimpleBounding b : boundings) {
            float bound = b.getZUpperBound();
            if (max < bound)
                max = bound;
        }

        return max;
    }

    public float getZLowerBound() {
        float min = Float.POSITIVE_INFINITY;
        for (SimpleBounding b : boundings) {
            float bound = b.getZLowerBound();
            if (min > bound)
                min = bound;
        }

        return min;
    }
}
