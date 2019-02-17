package object;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import engine.PlayableObject;
import core.Quaternion;
import core.Point3;

public class Pawn extends PlayableObject {

    private static boolean TEST_DOUBLEHEAD = false;
    private float height, radius;

    public Pawn(Point3 position, float yaw, float height, float radius) {
        super(position, Quaternion.fromYaw(yaw), 2);
        this.height = height;
        this.radius = radius;
    }

    public void draw(GL2 gl, GLUT glut) {}

    public void drawTransformed(GL2 gl, GLUT glut) {
        float coneHeight = height - radius;

        gl.glTranslatef(0, 0, height/-2);
        gl.glColor3f(0f, 0.5f, 0f);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        glut.glutSolidCone(radius, coneHeight, 12, 4);
        gl.glTranslatef(0.0f, 0.0f, coneHeight);

        if (TEST_DOUBLEHEAD) {
            gl.glTranslatef(0.0f, 0.1f, 0.0f);
            gl.glColor3f(0f, 0f, 0.5f);
            glut.glutSolidSphere(radius, 12, 4);
            gl.glTranslatef(0.0f, -0.2f, 0.0f);
            gl.glColor3f(0.5f, 0f, 0f);
            glut.glutSolidSphere(radius, 12, 4);
            gl.glTranslatef(0.0f, 0.1f, 0.0f);
        } else
            glut.glutSolidSphere(radius, 12, 4);

        gl.glTranslatef(0, 0, height/2 - coneHeight);
    }
}