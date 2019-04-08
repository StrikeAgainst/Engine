package engine;

import core.*;
import engine.collision.BroadPhaseContact;
import engine.collision.Contact;
import engine.collision.ContactProperties;
import engine.collision.ObjectContact;
import engine.collision.bounding.*;

import java.util.ArrayList;

public abstract class RigidObject extends Collidable {

	private static int ID_INCREMENT = 0;
	
	protected int id;
	protected boolean visible = true;

	protected Vector3 velocity = new Vector3(), lastVelocity = new Vector3(), acceleration = new Vector3(), lastAcceleration = new Vector3(), rotation = new Vector3(), totalForce = new Vector3(), totalTorque = new Vector3();
	protected Transformation transformation;
	protected InertiaTensor inertiaTensor = null;
	protected ObjectBounding bounding = null;
	protected boolean internalsUpdated = false;
	protected RGB color = null, gridColor = null;

    public RigidObject(Transformation transformation) {
        this.id = RigidObject.ID_INCREMENT++;
        this.transformation = transformation;
		updateInternals();
    }

    public void initBounding(ObjectBounding bounding) {
		if (this.bounding == null)
			this.bounding = bounding;
	}

	public void update(double tick) {
		internalsUpdated = false;

		if (inverseMass == 0) {
			lastAcceleration.nullify();
			acceleration.nullify();
			lastVelocity.nullify();
			velocity.nullify();
			rotation.nullify();
		} else {
			lastAcceleration = acceleration;
			lastAcceleration = Vector3.sum(lastAcceleration, totalForce.scaled(inverseMass));
			lastVelocity = velocity;
			velocity.add(lastAcceleration.scaled(tick));
			Point3 position = transformation.getPosition().offset(Vector3.sum(lastVelocity.scaled(tick), lastAcceleration.scaled(tick * tick / 2)));

			Vector3 angularAcc = inertiaTensor.productInverseGlobal(totalTorque);
			rotation.add(angularAcc.scaled(tick));
			Quaternion orientation = transformation.getOrientation().sum(rotation.scaled(tick));

			set(position, orientation);

			if (!velocity.isNull() || !rotation.isNull())
				updateInternals();
		}

		totalForce.nullify();
		totalTorque.nullify();
	}

	public void updateInternals() {
		if (inertiaTensor != null)
			inertiaTensor.updateInverseGlobal(transformation);

		if (bounding != null)
			bounding.updateAll();

		internalsUpdated = true;
	}

	public void applyForce(Vector3 force) {
		totalForce.add(force);
	}

	public void applyTorque(Vector3 torque) {
		totalTorque.add(torque);
	}

	public void applyForce(Vector3 force, Point3 p) {
		totalForce.add(force);
		totalTorque.add(Vector3.cross(Vector3.offset(transformation.getPosition(), p), force));
	}

	public void applyForceLocal(Vector3 force, Point3 p) {
		applyForce(force, transformation.toGlobal(p));
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

	public Vector3 getRotation() {
		return rotation;
	}

	public void setVelocity(Vector3 velocity) {
		this.velocity.set(velocity);
	}

	public void setAcceleration(Vector3 acceleration) {
		this.acceleration.set(acceleration);
	}

	public void setRotation(Vector3 rotation) {
		this.rotation.set(rotation);
	}

	public void addVelocity(Vector3 velocity) {
		this.velocity.add(velocity);
	}

	public void addAcceleration(Vector3 acceleration) {
		this.acceleration.add(acceleration);
	}

	public void addRotation(Vector3 rotation) {
		this.rotation.add(rotation);
	}

	public void stop() {
		velocity.nullify();
		acceleration.nullify();
		rotation.nullify();
	}

	public double getMass() {
		if (inverseMass == 0)
			return Double.POSITIVE_INFINITY;
		return 1/inverseMass;
	}

	public InertiaTensor getInertiaTensor() {
		return inertiaTensor;
	}

	public ArrayList<Contact> contactsWith(Collidable collidable) {
		ArrayList<Contact> contacts = new ArrayList<>();
		if (bounding == null || collidable.getBounding() == null || (immovable() && collidable.immovable()))
			return contacts;

		if (collidable instanceof RigidObject) {
			BroadPhaseContact broadphaseContact = bounding.broadphaseContactWith(((RigidObject) collidable).getBounding());
			if (broadphaseContact == null)
				return contacts;

			//contacts.add(broadphaseContact);
		}

		for (ContactProperties properties : bounding.contactsWith(collidable.getBounding()))
			contacts.add(new ObjectContact(this, collidable, properties));

		return contacts;
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
		String attributes = "transformation="+transformation.toString();
		if (!immovable())
			attributes += ",velocity="+velocity.toString()+",acceleration="+acceleration.toString();

		return attributes;
	}
	
	public String toString() {
		return getNameIDString()+":["+getAttributesString()+"]";
	}
}
