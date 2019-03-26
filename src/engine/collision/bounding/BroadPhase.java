package engine.collision.bounding;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import core.Point3;
import core.RGB;
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
        double upperX = Math.max(bp1.getXUpperBound(), bp2.getXUpperBound());
        double upperY = Math.max(bp1.getYUpperBound(), bp2.getYUpperBound());
        double upperZ = Math.max(bp1.getZUpperBound(), bp2.getZUpperBound());

        double lowerX = Math.min(bp1.getXLowerBound(), bp2.getXLowerBound());
        double lowerY = Math.min(bp1.getYLowerBound(), bp2.getYLowerBound());
        double lowerZ = Math.min(bp1.getZLowerBound(), bp2.getZLowerBound());

        return new BroadPhase(new Point3(upperX, upperY, upperZ), new Point3(lowerX, lowerY, lowerZ));
    }

    public void render(GL2 gl, GLUT glut) {
        if (VISIBLE) {
            if (vertices == null)
                vertices = Point3.getVertexMap(upperBound, lowerBound);

            RGB outline;
            if (inContact())
                outline = new RGB(1, 0.55, 0);
            else
                outline = new RGB(0, 0, 1);
            Renderer.renderLineQuad(gl, glut, vertices, outline);
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

    public double getXUpperBound() {
        return upperBound.getX();
    }

    public double getXLowerBound() {
        return lowerBound.getX();
    }

    public double getYUpperBound() {
        return upperBound.getY();
    }

    public double getYLowerBound() {
        return lowerBound.getY();
    }

    public double getZUpperBound() {
        return upperBound.getZ();
    }

    public double getZLowerBound() {
        return lowerBound.getZ();
    }

    public String getAttributesString() {
        return "upperBound:"+(upperBound == null?"null":upperBound.toString())+", lowerBound:"+(lowerBound == null?"null":lowerBound.toString());
    }
}
