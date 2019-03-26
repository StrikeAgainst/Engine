package engine.collision.bounding;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import engine.Transformation;
import engine.RigidObject;

public class BoundingSphere extends SimpleBounding {

    private double radius;

    public BoundingSphere(RigidObject object, double radius) {
        super(object);
        this.radius = radius;
    }

    public BoundingSphere(RigidObject object, Transformation transformation, double radius) {
        super(object, transformation);
        this.radius = radius;
    }

    public void draw(GL2 gl, GLUT glut) {}

    public void drawTransformed(GL2 gl, GLUT glut) {
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
        glut.glutWireSphere(radius, 8, 8);
    }

    public double getRadius() {
        return radius;
    }

    public double getXUpperBound() {
        return getPosition().getX()+radius;
    }

    public double getXLowerBound() {
        return getPosition().getX()-radius;
    }

    public double getYUpperBound() {
        return getPosition().getY()+radius;
    }

    public double getYLowerBound() {
        return getPosition().getY()-radius;
    }

    public double getZUpperBound() {
        return getPosition().getZ()+radius;
    }

    public double getZLowerBound() {
        return getPosition().getZ()-radius;
    }

    public String getAttributesString() {
        return "radius="+radius+", "+super.getAttributesString();
    }
}
