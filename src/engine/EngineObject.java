package engine;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import engine.collision.Collision;
import world.Point3D;
import world.bounding.Bounding;
import world.bounding.BoundingProperties;

public abstract class EngineObject {

	public static int ID_INCREMENT = 0;
	public static boolean SHOW_BOUNDING = false;
	public static float friction = 0.4f;
	
	protected int id;
	protected boolean visible = true;
	protected boolean gravitational = false;
	protected boolean highlight = false;
	protected float inverse_mass = 0f;
	protected float ya = 0, za = 60;
	protected Point3D anchor;
	protected Bounding bounding, broadPhase;
	
	public EngineObject(Point3D anchor, BoundingProperties boundingProperties) {
		this.id = EngineObject.ID_INCREMENT++;
		this.anchor = anchor;
        this.bounding = new Bounding(anchor, boundingProperties);
        this.broadPhase = getBounding();
        ObjectContainer.get().add(this);
	}
	
	public static void toggleShowBounding() {
		EngineObject.SHOW_BOUNDING = !EngineObject.SHOW_BOUNDING;
	}


	// draw

	protected abstract void draw(GL2 gl, GLUT glut);
	
	public void drawObject(GL2 gl, GLUT glut) {
		if (visible)
            this.draw(gl, glut);
		if (SHOW_BOUNDING && bounding != null)
			bounding.draw(gl, glut, highlight);
	}
	
	public void update(float tick) {
		highlight = false;
	}


	// getter/setter

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
	
	public Bounding getBounding() {
        return bounding;
    }

    public Bounding getBroadPhase() {
        return broadPhase;
    }

    public boolean isVisible() {
        return visible;
    }
	
	public void setVisible() {
		visible = true;
	}
	
	public void setInvisible() {
		visible = false;
	}


	//miscellaneous

	public boolean isMoving() {
		return false;
	}
	
	public void highlight() {
		highlight = true;
	}
	
	public Collision collides(EngineObject object) {
		if (object instanceof PhysicalObject)
			return object.collides(this);
		return null;
	}
	
	public boolean isGravitational() {
		return gravitational;
	}
	
	public void destroy() {
		ObjectContainer.get().remove(this);
	}
	
	public int getID() {
		return id;
	}
	
	public String getNameString() {
		return getClass().getSimpleName();
	}
	
	public String getAttributesString() {
		return "[x="+anchor.getX()+", y="+anchor.getY()+", z="+anchor.getZ()+"]";
	}
	
	public String toString() {
		return getNameString()+":"+getID()+":"+getAttributesString();
	}
}
