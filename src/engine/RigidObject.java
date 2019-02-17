package engine;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import engine.collision.Collision;
import core.*;
import engine.collision.bounding.*;

public abstract class RigidObject {

	private static int ID_INCREMENT = 0;
	
	protected int id;
	protected boolean visible = true;
	protected static float friction, restitution;

	protected Vector3 velocity = new Vector3(), acceleration = new Vector3(), totalForce = new Vector3(), totalTorque = new Vector3(), rotation = new Vector3();
	protected Transformation transformation;
	protected ObjectBounding bounding = null;
	protected float inverseMass = 0;
	protected boolean internalsUpdated = false;

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
			gl.glMultMatrixf(transformation.getMatrix().getDataLinear(false, true), 0);

			this.drawTransformed(gl, glut);
			if (bounding != null)
				bounding.render(gl, glut);

			gl.glPopMatrix();

		}
	}

	public void transformInertiaTensor() {}

	public void update(float tick) {
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

	public void setVelocity(Vector3 velocity) {}

	public Vector3 getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector3 acceleration) {}

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

	public float getInverseMass() {
		return 0;
	}

	public Collision collides(RigidObject object) {
		if (object instanceof PhysicalObject)
			return object.collides(this);
		return null;
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
	
	public int getID() {
		return id;
	}
	
	public String getNameString() {
		return getClass().getSimpleName();
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
