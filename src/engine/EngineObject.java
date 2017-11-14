package engine;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public abstract class EngineObject {

	public static boolean SHOW_BOUNDING = false;
	protected Point3D center;
	protected float vx = 0, vy = 0, vz = 0, ya = 0, za = 60;
	protected boolean visible = true;
	protected Bounding bounding = null;
	
	public EngineObject(Point3D center) {
		this.center = center;
		ObjectContainer.get().add(this);
	}
	
	protected final void applyBounding(Bounding bounding) {
		this.bounding = bounding;
	};
	
	public static void toggleShowBounding() {
		EngineObject.SHOW_BOUNDING = !EngineObject.SHOW_BOUNDING;
	}
	
	protected abstract void draw(GL2 gl, GLUT glut);
	
	public void drawObject(GL2 gl, GLUT glut) {
		if (visible)
			this.draw(gl, glut);
		if (SHOW_BOUNDING && bounding != null)
			bounding.draw(gl, glut);
	}
	
	public Point3D getCenter() {
		return center;
	}
	
	public void setCenter(Point3D center) {
		this.center = center;
	}
	
	public float getX() {
		return center.getX();
	}
	
	public void setX(float x) {
		center.setX(x);
	}
	
	public float getY() {
		return center.getY();
	}
	
	public void setY(float y) {
		center.setY(y);
	}
	
	public float getZ() {
		return center.getZ();
	}
	
	public void setZ(float z) {
		center.setZ(z);
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
	
	public Bounding getBounding(){
		return bounding;
	}
	
	public void setVisible() {
		visible = true;
	}
	
	public void setInvisible() {
		visible = false;
	}
	
	public String toString() {
		return "[x="+center.getX()+", y="+center.getY()+", z="+center.getZ()+", ya="+ya+", za="+za+"]";
	}
}
