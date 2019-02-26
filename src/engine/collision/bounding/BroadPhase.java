package engine.collision.bounding;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import core.Point3;
import engine.collision.BroadPhaseContact;
import engine.collision.Contact;
import engine.collision.ContactContainer;
import engine.collision.ContactDetector;
import main.Renderer;

public class BroadPhase extends Bounding {

    public static boolean VISIBLE = false;

    private ObjectBounding bounding = null;
    private Point3[] vertices;
    private Point3 upperBound, lowerBound;

    public BroadPhase(ObjectBounding bounding) {
        this.bounding = bounding;
    }

    public BroadPhase(Point3 upperBound, Point3 lowerBound) {
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
    }

    public static BroadPhase combine(BroadPhase bp1, BroadPhase bp2) {
        float upperX = Math.max(bp1.getXUpperBound(), bp2.getXUpperBound());
        float upperY = Math.max(bp1.getYUpperBound(), bp2.getYUpperBound());
        float upperZ = Math.max(bp1.getZUpperBound(), bp2.getZUpperBound());

        float lowerX = Math.min(bp1.getXLowerBound(), bp2.getXLowerBound());
        float lowerY = Math.min(bp1.getYLowerBound(), bp2.getYLowerBound());
        float lowerZ = Math.min(bp1.getZLowerBound(), bp2.getZLowerBound());

        return new BroadPhase(new Point3(upperX, upperY, upperZ), new Point3(lowerX, lowerY, lowerZ));
    }

    public void render(GL2 gl, GLUT glut) {
        if (VISIBLE) {
            if (vertices == null)
                vertices = Point3.getVertexMap(upperBound, lowerBound);

            if (inContact())
                gl.glColor3f(1f, 0.55f, 0f);
            else
                gl.glColor3f(0, 0, 1f);
            Renderer.renderLineQuad(gl, glut, vertices);
        }
    }

    public boolean inContact() {
        for (Contact c : ContactContainer.get())
            if (c.involves(this))
                return true;

        return false;
    }

    public void update() {
        if (bounding == null)
            return;

        upperBound = new Point3(bounding.getXUpperBound(), bounding.getYUpperBound(), bounding.getZUpperBound());
        lowerBound = new Point3(bounding.getXLowerBound(), bounding.getYLowerBound(), bounding.getZLowerBound());

        vertices = null;
    }

    public BroadPhaseContact contactWith(BroadPhase broadPhase) {
        return ContactDetector.BroadPhaseOnBroadPhase(this, broadPhase);
    }

    public Point3 getUpperBound() {
        return upperBound;
    }

    public Point3 getLowerBound() {
        return lowerBound;
    }

    public ObjectBounding getBounding() {
        return bounding;
    }

    public float getXUpperBound() {
        return upperBound.getX();
    }

    public float getXLowerBound() {
        return lowerBound.getX();
    }

    public float getYUpperBound() {
        return upperBound.getY();
    }

    public float getYLowerBound() {
        return lowerBound.getY();
    }

    public float getZUpperBound() {
        return upperBound.getZ();
    }

    public float getZLowerBound() {
        return lowerBound.getZ();
    }

    public String getAttributesString() {
        return "upperBound:"+(upperBound == null?"null":upperBound.toString())+", lowerBound:"+(lowerBound == null?"null":lowerBound.toString());
    }
}
