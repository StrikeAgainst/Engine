package engine.collision.bounding;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import core.Point3;
import core.RGB;
import core.Vector3;
import main.Renderer;

public class OctreeBounding extends Bounding {

    public static boolean VISIBLE = false;
    
    private double size, halfSize;
    private Point3[] vertices;
    private Point3 position;

    public OctreeBounding(Point3 position, double size) {
        this.position = position;
        this.size = size;
        this.halfSize = size/2;

        Point3[] verticesLocal = new Point3[] {
                new Point3(halfSize, halfSize, halfSize),
                new Point3(halfSize, halfSize, -halfSize),
                new Point3(halfSize, -halfSize, -halfSize),
                new Point3(halfSize, -halfSize, halfSize),
                new Point3(-halfSize, -halfSize, halfSize),
                new Point3(-halfSize, -halfSize, -halfSize),
                new Point3(-halfSize, halfSize, -halfSize),
                new Point3(-halfSize, halfSize, halfSize)
        };

        vertices = new Point3[8];
        for (int i = 0; i < 8; i++)
            vertices[i] = position.offset(new Vector3(verticesLocal[i]));
    }

    public void render(GL2 gl, GLUT glut) {
        if (VISIBLE)
            Renderer.renderLineQuad(gl, glut, vertices, new RGB(0,0, 0.6));
    }

    public boolean encloses(BroadPhase bp) {
        double oXupper = getXUpperBound(), oXlower = getXLowerBound();
        double oYupper = getYUpperBound(), oYlower = getYLowerBound();
        double oZupper = getZUpperBound(), oZlower = getZLowerBound();

        double bXupper = bp.getXUpperBound(), bXlower = bp.getXLowerBound();
        double bYupper = bp.getYUpperBound(), bYlower = bp.getYLowerBound();
        double bZupper = bp.getZUpperBound(), bZlower = bp.getZLowerBound();

        return (oXlower <= bXlower && bXupper <= oXupper && oYlower <= bYlower && bYupper <= oYupper && oZlower <= bZlower && bZupper <= oZupper);
    }

    public Point3 getPosition() {
        return position;
    }

    public double getXUpperBound() {
        return position.getX()+halfSize;
    }

    public double getXLowerBound() {
        return position.getX()-halfSize;
    }

    public double getYUpperBound() {
        return position.getY()+halfSize;
    }

    public double getYLowerBound() {
        return position.getY()-halfSize;
    }

    public double getZUpperBound() {
        return position.getZ()+halfSize;
    }

    public double getZLowerBound() {
        return position.getZ()-halfSize;
    }

    public String getAttributesString() {
        return "position:"+position.toString()+", size:"+size;
    }
}
