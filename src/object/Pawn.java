package object;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import engine.PlayableObject;
import world.bounding.AABBProperties;
import world.Point3D;

public class Pawn extends PlayableObject {

    private float bodyHeight, radius;

    public Pawn(Point3D anchor, float bodyHeight, float radius) {
        super(anchor, new AABBProperties(radius, radius, (bodyHeight + radius) / 2));
        this.bodyHeight = bodyHeight;
        this.radius = radius;
    }

    public void draw(GL2 gl, GLUT glut) {
        gl.glTranslatef(anchor.getX(), anchor.getY(), anchor.getZ() - (bodyHeight + radius) / 2);
        gl.glRotatef(ya, 0.0f, 0.0f, 1.0f);
        gl.glColor3f(0f, 0.6f, 0f);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        glut.glutSolidCone(radius, bodyHeight, 12, 4);
        gl.glTranslatef(0.0f, 0.0f, bodyHeight);
        glut.glutSolidSphere(radius, 12, 4);
        gl.glRotatef(-ya, 0.0f, 0.0f, 1.0f);
        gl.glTranslatef(anchor.getX() * (-1), anchor.getY() * (-1), (anchor.getZ() - (bodyHeight + radius) / 2 + bodyHeight) * (-1));
    }

    public float getCameraX() {
        return anchor.getX();
    }

    public float getCameraY() {
        return anchor.getY();
    }

    public float getCameraZ() {
        return anchor.getZ();
    }
}