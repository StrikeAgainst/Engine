package object;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import engine.PhysicalObject;
import world.BoundingSphere;
import world.Point3D;

public class Ball extends PhysicalObject {
	
	public float radius;
	
	public Ball(Point3D anchor, float radius) {
		super(anchor, new BoundingSphere(anchor, radius));
		this.radius = radius;
	}
	
	public void draw(GL2 gl, GLUT glut) {
		gl.glTranslatef(anchor.getX(), anchor.getY(), anchor.getZ());
		gl.glColor3f(1f,0f,0f);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
		glut.glutSolidSphere(radius, 12, 4);
		gl.glTranslatef(anchor.getX()*(-1), anchor.getY()*(-1), (anchor.getZ())*(-1));
	}
}
