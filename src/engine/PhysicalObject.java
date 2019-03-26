package engine;

import engine.collision.*;
import core.*;

import java.util.ArrayList;

public abstract class PhysicalObject extends RigidObject {

	protected InertiaTensor inertiaTensor;

	public PhysicalObject(Point3 position, Quaternion orientation, double mass) {
		this(position, orientation, mass, InertiaTensorFactory.forDefault(mass));
	}

	public PhysicalObject(Point3 position, Quaternion orientation, double mass, InertiaTensor inertiaTensor) {
		super(position, orientation);

		this.inverseMass = 1 / mass;
		this.inertiaTensor = inertiaTensor;
		this.inertiaTensor.updateInverseGlobal(transformation);
	}

	public void update(double tick) {
		super.update(tick);

		lastAcceleration = acceleration;
		lastAcceleration = Vector3.sum(lastAcceleration, totalForce.scaled(inverseMass));
		lastVelocity = velocity;
		velocity.add(lastAcceleration.scaled(tick));
		Point3 position = transformation.getPosition().offset(Vector3.sum(lastVelocity.scaled(tick), lastAcceleration.scaled(tick*tick/2)));

		Vector3 angularAcc = inertiaTensor.productInverseGlobal(totalTorque);
		rotation.add(angularAcc.scaled(tick));
		Quaternion orientation = transformation.getOrientation().sum(rotation.scaled(tick));

		set(position, orientation);

		clearForce();
		clearTorque();

		if (!velocity.isNull() || !rotation.isNull())
			updateInternals();
	}

	public void updateInternals() {
		if (inertiaTensor != null)
			inertiaTensor.updateInverseGlobal(transformation);
		super.updateInternals();
	}
	
	public ArrayList<Contact> contactsWith(Collidable collidable) {
		ArrayList<Contact> contacts = new ArrayList<>();
		if (bounding == null || collidable.getBounding() == null)
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

	public boolean hasRestingContact() {
		// todo
		return true;
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

	public boolean isAccelerating() {
		return !acceleration.isNull();
	}

	public void stop() {
		velocity.nullify();
		acceleration.nullify();
		rotation.nullify();
	}

	public double getMass() {
		return 1/inverseMass;
	}

	public InertiaTensor getInertiaTensor() {
		return inertiaTensor;
	}

	public boolean isGravitated() {
		return true;
	}

	public String getAttributesString() {
		return super.getAttributesString()+",velocity="+velocity.toString()+",acceleration="+acceleration.toString();
	}
}
