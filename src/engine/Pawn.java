package engine;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Pawn extends PlayablePhysicsObject {

	private float bodyHeight, radius;
			
	public Pawn(Point3D center, float bodyHeight, float radius) {
		super(center);
		this.bodyHeight = bodyHeight;
		this.radius = radius;
		applyBoundingBox(new CuboidBoundingBox(this, radius, -radius, radius, -radius, bodyHeight+radius, 0));
	}
	
	public void draw(GL2 gl, GLUT glut) {
		gl.glTranslatef(center.getX(), center.getY(), center.getZ());
		gl.glRotatef(ya, 0.0f, 0.0f, 1.0f);
		gl.glColor3f(0f,0.6f,0f);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
		glut.glutSolidCone(radius, bodyHeight, 12, 4);
		gl.glTranslatef(0.0f, 0.0f, bodyHeight);
		glut.glutSolidSphere(radius, 12, 4);
		gl.glRotatef(-ya, 0.0f, 0.0f, 1.0f);
		gl.glTranslatef(center.getX()*(-1), center.getY()*(-1), (center.getZ()+bodyHeight)*(-1));
	}
	
	public float getCamX() {
		return center.getX();
	}
	
	public float getCamY() {
		return center.getY();
	}
	
	public float getCamZ() {
		return center.getZ()+bodyHeight;
	}
	
	public String toString() {
		return "Pawn:"+super.toString();
	}
}
