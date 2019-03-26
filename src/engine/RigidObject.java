package engine;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import core.*;
import engine.collision.Contact;
import engine.collision.bounding.*;

import java.util.ArrayList;

public abstract class RigidObject extends Collidable {

	private static int ID_INCREMENT = 0;
	
	protected int id;
	protected boolean visible = true;
	protected static double friction, restitution;

	protected Vector3 velocity = new Vector3(), lastVelocity = new Vector3(), acceleration = new Vector3(), lastAcceleration = new Vector3(), rotation = new Vector3(), totalForce = new Vector3(), totalTorque = new Vector3();
	protected Transformation transformation;
	protected ObjectBounding bounding = null;
	protected Material material = null;
	protected double inverseMass = 0;
	protected boolean internalsUpdated = false;
	protected RGB color = null, gridColor = null;

	public RigidObject(Point3 position, Quaternion orientation) {
		this(new Transformation(position, orientation));
	}

    public RigidObject(Transformation transformation) {
        this.id = RigidObject.ID_INCREMENT++;
        this.transformation = transformation;
		updateInternals();
    }

    public void initBounding(ObjectBounding bounding) {
		if (this.bounding == null)
			this.bounding = bounding;
	}

	protected abstract void draw(GL2 gl, GLUT glut);

	protected abstract void drawTransformed(GL2 gl, GLUT glut);

	public void render(GL2 gl, GLUT glut) {
		if (visible) {
			this.draw(gl, glut);
			if (bounding != null && BroadPhase.VISIBLE)
				bounding.getBroadPhase().render(gl, glut);

			gl.glPushMatrix();
			gl.glMultMatrixd(transformation.getMatrix().getDataLinear(false, true), 0);

			this.drawTransformed(gl, glut);
			if (bounding != null)
				bounding.render(gl, glut);

			gl.glPopMatrix();

		}
	}

	public void update(double tick) {
		internalsUpdated = false;
	}

	public void updateInternals() {
		if (bounding != null)
			bounding.updateAll();
		internalsUpdated = true;
	}

	public void destroy() {
		this.bounding = null;
		//todo
	}

	public void add(Vector3 p, Vector3 q) {
		transformation.add(p, q);
		updateInternals();
	}

	public void addPosition(Vector3 p) {
		transformation.addPosition(p);
		updateInternals();
	}

	public void addOrientation(Vector3 q) {
		transformation.addOrientation(q);
		updateInternals();
	}

	public void set(Point3 p, Quaternion q) {
		transformation.set(p, q);
		updateInternals();
	}
	
	public void setPosition(Point3 p) {
        transformation.setPosition(p);
		updateInternals();
	}

	public void setOrientation(Quaternion q) {
        transformation.setOrientation(q);
		updateInternals();
	}

	public Quaternion getOrientation() {
		return transformation.getOrientation();
	}

	public Point3 getPosition() {
        return transformation.getPosition();
	}

	public Transformation getTransformation() {
		return transformation;
	}

	public Vector3 getVelocity() {
		return velocity;
	}

	public Vector3 getLastVelocity() {
		return lastVelocity;
	}

	public void setVelocity(Vector3 velocity) {}

	public void addVelocity(Vector3 velocity) {}

	public Vector3 getPointVelocity(Point3 point) {
		return Vector3.sum(velocity, Vector3.cross(rotation, Vector3.offset(transformation.getPosition(), point)));
	}

	public Vector3 getPointVelocityLocal(Point3 point) {
		return getPointVelocity(transformation.toGlobal(point));
	}

	public Vector3 getAcceleration() {
		return acceleration;
	}

	public Vector3 getLastAcceleration() {
		return lastAcceleration;
	}

	public void setAcceleration(Vector3 acceleration) {}

	public void addAcceleration(Vector3 acceleration) {}

	public Vector3 getRotation() {
		return rotation;
	}

	public void setRotation(Vector3 rotation) {}

	public void addRotation(Vector3 rotation) {}

	public boolean isAccelerating() {
		return false;
	}

	public void stop() {}

	public void clearForce() {
		totalForce.nullify();
	}

	public void clearTorque() {
		totalTorque.nullify();
	}

	public double getMass() {
		return Double.POSITIVE_INFINITY;
	}

	public double getInverseMass() {
		return inverseMass;
	}

	public ArrayList<Contact> contactsWith(Collidable collidable) {
		if (collidable instanceof PhysicalObject)
			return collidable.contactsWith(this);
		return new ArrayList<>();
	}

	public boolean isGravitated() {
		return false;
	}

	public boolean internalsUpdated() {
		return internalsUpdated;
	}
	
	public ObjectBounding getBounding() {
        return bounding;
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

	public void setColor(RGB color) {
		this.color = color;
	}

	public RGB getColor() {
		return color;
	}

	public void setGridColor(RGB gridColor) {
		this.gridColor = gridColor;
	}
	
	public int getID() {
		return id;
	}

	public String getNameIDString() {
		return getNameString()+":"+getID();
	}
	
	public String getAttributesString() {
		return "transformation="+transformation.toString();
	}
	
	public String toString() {
		return getNameIDString()+":["+getAttributesString()+"]";
	}
}
