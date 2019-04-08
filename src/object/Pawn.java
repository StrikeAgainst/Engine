package object;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import core.RGB;
import engine.PlayableObject;
import core.Quaternion;
import core.Point3;
import engine.Transformation;

public class Pawn extends PlayableObject {

    private double height, radius;
    protected RGB color = new RGB(0, 0.5, 0);

    public Pawn(Point3 position, double yaw, double height, double radius) {
        super(new Transformation(position, Quaternion.fromYaw(yaw)), 2);
        this.height = height;
        this.radius = radius;
    }

    public void draw(GL2 gl, GLUT glut) {}

    public void drawTransformed(GL2 gl, GLUT glut) {
        double coneHeight = height - radius;

        gl.glTranslated(0, 0, height/-2);
        color.setForGL(gl);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        glut.glutSolidCone(radius, coneHeight, 12, 4);
        gl.glTranslated(0.0, 0.0, coneHeight);

        glut.glutSolidSphere(radius, 12, 4);

        gl.glTranslated(0, 0, height/2 - coneHeight);
    }
}