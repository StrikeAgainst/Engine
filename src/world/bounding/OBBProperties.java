package world.bounding;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import world.ONB3D;
import world.Point3D;
import world.Vector3D;

public class OBBProperties extends BoundingProperties {

    protected ONB3D onb;
    protected float x, y, z;

    public OBBProperties(ONB3D onb, float x, float y, float z) {
        super();
        this.onb = onb;
        this.x = Math.abs(x);
        this.y = Math.abs(y);
        this.z = Math.abs(z);
    }

    public void draw(Point3D anchor, GL2 gl, GLUT glut, boolean highlight) {
        Point3D[] points = getVertices(anchor);
        if (highlight)
            gl.glColor3f(1f,0.55f,0f);
        else
            gl.glColor3f(r, g, b);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
        gl.glBegin(GL2.GL_QUADS);

        for (int quad : new int[] {0,1,2,3,2,3,4,5,4,5,6,7,6,7,0,1})
            gl.glVertex3f(points[quad].getX(), points[quad].getY(), points[quad].getZ());
        gl.glEnd();
    }

    public boolean encloses(Point3D anchor, Point3D point) {
        Vector3D v = anchor.getVector(point);
        //todo: implement
        return false;
    }

    public boolean encloses(Point3D anchor, Bounding bounding) {
        //todo: implement
        return false;
    }

    public Vector3D intersects(Point3D anchor, Bounding bounding) {
        //todo: implement
        return null;
    }

    public Point3D[] getVertices(Point3D anchor) {
        //todo: implement
        return null;
    }

    public float getFrontBound(Point3D anchor) {
        //todo: implement
        return 0;
    }

    public float getBackBound(Point3D anchor) {
        //todo: implement
        return 0;
    }

    public float getLeftBound(Point3D anchor) {
        //todo: implement
        return 0;
    }

    public float getRightBound(Point3D anchor) {
        //todo: implement
        return 0;
    }

    public float getTopBound(Point3D anchor) {
        //todo: implement
        return 0;
    }

    public float getBottomBound(Point3D anchor) {
        //todo: implement
        return 0;
    }

    public String getType() {
        return "OBB";
    }

    public String toString() {
        //todo: implement
        return "OBBProperties:[x="+x+", y="+y+", z="+z+"]";
    }
}
