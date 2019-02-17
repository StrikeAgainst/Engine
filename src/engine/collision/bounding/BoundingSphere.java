package engine.collision.bounding;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import engine.Transformation;
import engine.RigidObject;

public class BoundingSphere extends PrimitiveBounding {

    private float radius;

    public BoundingSphere(RigidObject object, float radius) {
        super(object);
        this.radius = radius;
    }

    public BoundingSphere(RigidObject object, Transformation transformation, float radius) {
        super(object, transformation);
        this.radius = radius;
    }

    public void draw(GL2 gl, GLUT glut) {}

    public void drawTransformed(GL2 gl, GLUT glut) {
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
        glut.glutWireSphere(radius, 8, 8);
    }

    public float getRadius() {
        return radius;
    }

    public float getXUpperBound() {
        return getPosition().getX()+radius;
    }

    public float getXLowerBound() {
        return getPosition().getX()-radius;
    }

    public float getYUpperBound() {
        return getPosition().getY()+radius;
    }

    public float getYLowerBound() {
        return getPosition().getY()-radius;
    }

    public float getZUpperBound() {
        return getPosition().getZ()+radius;
    }

    public float getZLowerBound() {
        return getPosition().getZ()-radius;
    }

    public String getAttributesString() {
        return "radius="+radius+", "+super.getAttributesString();
    }
}
