package object;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import core.RGB;
import engine.PhysicalObject;
import engine.InertiaTensorFactory;
import core.Quaternion;
import core.Point3;

public class Ball extends PhysicalObject {
	
	private double radius;
	
	public Ball(Point3 position, double radius, double mass) {
		super(position, new Quaternion(), mass, InertiaTensorFactory.forSphere(mass, radius, true));
		this.radius = radius;
		this.color = RGB.getRandom();
		this.gridColor = RGB.getRandom();
	}

	public void draw(GL2 gl, GLUT glut) {}
	
	public void drawTransformed(GL2 gl, GLUT glut) {
		color.setForGL(gl);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
		glut.glutSolidSphere(radius, 12, 4);

		gridColor.setForGL(gl);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
		glut.glutSolidSphere(radius, 12, 4);
	}
}
