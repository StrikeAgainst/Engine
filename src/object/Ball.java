package object;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import engine.PhysicalObject;
import engine.force.InertiaTensorFactory;
import core.Quaternion;
import core.Point3;

public class Ball extends PhysicalObject {
	
	public float radius;
	
	public Ball(Point3 position, float radius, float mass) {
		super(position, new Quaternion(), mass, InertiaTensorFactory.forSphere(mass, radius, true));
		this.radius = radius;
	}

	public void draw(GL2 gl, GLUT glut) {}
	
	public void drawTransformed(GL2 gl, GLUT glut) {
		gl.glColor3f(1f,0f,0f);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
		glut.glutSolidSphere(radius, 12, 4);
	}
}
