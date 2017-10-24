package engine;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public abstract class EngineObject {

	protected Point3D center;
	protected float x, y, z, vx = 0, vy = 0, vz = 0, ya = 0, za = 60;
	protected boolean visible = true, showBounds = false;
	protected BoundingBox bounds;
	
	public EngineObject(Point3D center) {
		this.center = center;
		ObjectContainer.get().add(this);
	}
	
	protected final void applyBoundingBox(BoundingBox bounds) {
		this.bounds = bounds;
	};
	
	protected abstract void draw(GL2 gl, GLUT glut);
	
	public void drawObject(GL2 gl, GLUT glut) {
		if (visible) {
			draw(gl, glut);
			if (showBounds) drawBounds(gl, glut);
		}
	}
	
	public void drawBounds(GL2 gl, GLUT glut) {
		if (showBounds) {
			Point3D[] points = bounds.getPoints();
			gl.glColor3f(1f,0.55f,0f);
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
			gl.glBegin(GL2.GL_QUAD_STRIP);
			for (int i = 0; i < points.length; i++) {
				gl.glVertex3f(points[i].getX(), points[i].getY(), points[i].getZ());
			}
			gl.glEnd();
		}
	};
	
	public Point3D getCenter() {
		return center;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		center = new Point3D(x,center.getY(),center.getZ());
	}
	
	public float getY() {
		return center.getY();
	}
	
	public void setY(float y) {
		center = new Point3D(center.getX(),y,center.getZ());
	}
	
	public float getZ() {
		return center.getZ();
	}
	
	public void setZ(float z) {
		center = new Point3D(center.getX(),center.getY(),z);
	}
	
	public float getVX() {
		return vx;
	}
	
	public void setVX(float vx) {
		this.vx = vx;
	}
	
	public float getVY() {
		return vy;
	}
	
	public void setVY(float vy) {
		this.vy = vy;
	}
	
	public float getVZ() {
		return vz;
	}
	
	public void setVZ(float vz) {
		this.vz = vz;
	}
	
	public float getYAngle() {
		return ya;
	}
	
	public void setYAngle(float ya) {
		this.ya = ya;
	}
	
	public float getZAngle() {
		return za; 
	}
	
	public void setZAngle(float za) {
		this.za = za;
	}
	
	public BoundingBox getBounds(){
		return bounds;
	}
	
	public void setVisible() {
		visible = true;
	}
	
	public void setInvisible() {
		visible = false;
	}
	
	public String toString() {
		return "[x="+x+", y="+y+", z="+z+", vx="+vx+", vy="+vy+", vz="+vz+", ya="+ya+", za="+za+", z="+z+"]";
	}
}
