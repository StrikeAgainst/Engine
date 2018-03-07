package engine;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import world.ObjectBounding;
import world.Point3D;
import world.Vector3D;

public abstract class EngineObject {

	public static int ID_INCREMENT = 0;
	public static boolean SHOW_BOUNDING = false;
	public static float restitution = 0.2f, friction = 0.4f;
	
	protected int id;
	protected boolean visible = true;
	protected boolean moving = false;
	protected boolean gravitational = false;
	protected Point3D anchor;
	protected ObjectBounding bounding = null;
	protected ArrayList<Intersection> intersections = new ArrayList<Intersection>();
	
	public EngineObject(Point3D anchor, ObjectBounding bounding) {
		this.id = EngineObject.ID_INCREMENT++;
		this.anchor = anchor;
		this.bounding = bounding;
		ObjectContainer.get().add(this);
	}
	
	public static void toggleShowBounding() {
		EngineObject.SHOW_BOUNDING = !EngineObject.SHOW_BOUNDING;
	}
	
	protected abstract void draw(GL2 gl, GLUT glut);
	
	public void drawObject(GL2 gl, GLUT glut) {
		if (visible)
			this.draw(gl, glut);
		if (SHOW_BOUNDING && bounding != null)
			bounding.draw(gl, glut, !intersections.isEmpty());
	}
	
	public Point3D getAnchor() {
		return anchor;
	}
	
	public void setAnchor(Point3D anchor) {
		this.anchor.moveTo(anchor);
	}
	
	public float getX() {
		return anchor.getX();
	}
	
	public void setX(float x) {
		anchor.setX(x);
	}
	
	public float getY() {
		return anchor.getY();
	}
	
	public void setY(float y) {
		anchor.setY(y);
	}
	
	public float getZ() {
		return anchor.getZ();
	}
	
	public void setZ(float z) {
		anchor.setZ(z);
	}
	
	public ObjectBounding getBounding(){
		return bounding;
	}
	
	public Vector3D intersects(EngineObject object){
		return bounding.intersects(object.getBounding());
	}
	
	public void resetIntersections() {
		intersections = new ArrayList<Intersection>();
	}
	
	public void addIntersection(Intersection intersection) {
		intersections.add(intersection);
	}
	
	public ArrayList<Intersection> getIntersections() {
		return intersections;
	}
	
	public boolean handleIntersections() {
		return true;
	};
	
	public void setVisible() {
		visible = true;
	}
	
	public void setInvisible() {
		visible = false;
	}
	
	public boolean isMoving() {
		return moving;
	}
	
	public boolean isGravitational() {
		return gravitational;
	}
	
	public int getID() {
		return id;
	}
	
	public String toString() {
		return id+":[x="+anchor.getX()+", y="+anchor.getY()+", z="+anchor.getZ()+"]";
	}
}
