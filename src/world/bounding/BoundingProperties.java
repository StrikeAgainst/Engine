package world.bounding;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import world.Point3D;
import world.Vector3D;

public abstract class BoundingProperties {

    // front > x > back
    // left > y > right
    // top > z > bottom

    protected float r = 0f, g = 0f, b = 1f;

    public BoundingProperties() {}

    public void setColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public abstract void draw(Point3D anchor, GL2 gl, GLUT glut, boolean highlight);

    public abstract boolean encloses(Point3D anchor, Point3D point);

    public abstract boolean encloses(Point3D anchor, Bounding bounding);

    public abstract Vector3D intersects(Point3D anchor, Bounding bounding);

    public abstract float getFrontBound(Point3D anchor);

    public abstract float getBackBound(Point3D anchor);

    public abstract float getLeftBound(Point3D anchor);

    public abstract float getRightBound(Point3D anchor);

    public abstract float getTopBound(Point3D anchor);

    public abstract float getBottomBound(Point3D anchor);

    public abstract String getType();
}