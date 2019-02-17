package engine.collision.bounding;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import core.Point3;
import core.Vector3;
import main.Renderer;

public class OctreeBounding extends Bounding {

    public static boolean VISIBLE = false;
    
    private float size, halfSize;
    private Point3[] vertices;
    private Point3 position;

    public OctreeBounding(Point3 position, float size) {
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
        if (VISIBLE) {
            gl.glColor3f(0, 0, 0.6f);
            Renderer.renderLineQuad(gl, glut, vertices);
        }
    }

    public boolean encloses(BroadPhase bp) {
        float oXupper = getXUpperBound(), oXlower = getXLowerBound();
        float oYupper = getYUpperBound(), oYlower = getYLowerBound();
        float oZupper = getZUpperBound(), oZlower = getZLowerBound();

        float bXupper = bp.getXUpperBound(), bXlower = bp.getXLowerBound();
        float bYupper = bp.getYUpperBound(), bYlower = bp.getYLowerBound();
        float bZupper = bp.getZUpperBound(), bZlower = bp.getZLowerBound();

        return (oXlower <= bXlower && bXupper <= oXupper && oYlower <= bYlower && bYupper <= oYupper && oZlower <= bZlower && bZupper <= oZupper);
    }

    public Point3 getPosition() {
        return position;
    }

    public float getXUpperBound() {
        return position.getX()+halfSize;
    }

    public float getXLowerBound() {
        return position.getX()-halfSize;
    }

    public float getYUpperBound() {
        return position.getY()+halfSize;
    }

    public float getYLowerBound() {
        return position.getY()-halfSize;
    }

    public float getZUpperBound() {
        return position.getZ()+halfSize;
    }

    public float getZLowerBound() {
        return position.getZ()-halfSize;
    }

    public String getAttributesString() {
        return "position:"+position.toString()+", size:"+size;
    }
}
