package engine;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Pawn extends PlayablePhysicsObject {

	private float bodyHeight, radius;
			
	public Pawn(float xPos, float yPos, float zPos, float bodyHeight, float radius) {
		super(xPos, yPos, zPos, radius, -radius, radius, -radius, bodyHeight+radius, 0);
		this.bodyHeight = bodyHeight;
		this.radius = radius;
	}
	
	public void draw(GL2 gl, GLUT glut) {
		gl.glTranslatef(x, y, z);
		gl.glRotatef(ya, 0.0f, 0.0f, 1.0f);
		gl.glColor3f(0f,0.6f,0f);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
		glut.glutSolidCone(radius, bodyHeight, 12, 4);
		gl.glTranslatef(0.0f, 0.0f, bodyHeight);
		glut.glutSolidSphere(radius, 12, 4);
		gl.glRotatef(-ya, 0.0f, 0.0f, 1.0f);
		gl.glTranslatef(-x, -y, -z-bodyHeight);
	}
	
	public float getCamX() {
		return x;
	}
	
	public float getCamY() {
		return y;
	}
	
	public float getCamZ() {
		return z+bodyHeight;
	}
	
	public String toString() {
		return "Pawn: "+super.toString();
	}
}
