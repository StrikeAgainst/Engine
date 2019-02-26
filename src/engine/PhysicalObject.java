package engine;

import engine.collision.*;
import core.*;
import engine.force.InertiaTensorFactory;

import java.util.ArrayList;

public abstract class PhysicalObject extends RigidObject {

	protected static float linear_damping = 0.995f, angular_damping = 0.995f;
	protected static Vector3 gravity = new Vector3(0,0, -9.807f);

	protected Matrix3x3 inverseInertiaTensorLocal, inverseInertiaTensorGlobal;

	public PhysicalObject(Point3 position, Quaternion orientation, float mass) {
		this(position, orientation, mass, InertiaTensorFactory.forDefault(mass));
	}

	public PhysicalObject(Point3 position, Quaternion orientation, float mass, Matrix3x3 inertiaTensor) {
		super(position, orientation);

		this.inverseMass = 1 / mass;
		this.inverseInertiaTensorLocal = inertiaTensor.getInverse();

		transformInertiaTensor();
	}

	public void transformInertiaTensor() {
		float[][] tData = transformation.getMatrix().getData();
		float[][] itlData = inverseInertiaTensorLocal.getData();

		float t00 = tData[0][0]*itlData[0][0] + tData[1][0]*itlData[0][1] + tData[2][0]*itlData[0][2];
		float t10 = tData[0][0]*itlData[1][0] + tData[1][0]*itlData[1][1] + tData[2][0]*itlData[1][2];
		float t20 = tData[0][0]*itlData[2][0] + tData[1][0]*itlData[2][1] + tData[2][0]*itlData[2][2];

		float t01 = tData[0][1]*itlData[0][0] + tData[1][1]*itlData[0][1] + tData[2][1]*itlData[0][2];
		float t11 = tData[0][1]*itlData[1][0] + tData[1][1]*itlData[1][1] + tData[2][1]*itlData[1][2];
		float t21 = tData[0][1]*itlData[2][0] + tData[1][1]*itlData[2][1] + tData[2][1]*itlData[2][2];

		float t02 = tData[0][2]*itlData[0][0] + tData[1][2]*itlData[0][1] + tData[2][2]*itlData[0][2];
		float t12 = tData[0][2]*itlData[1][0] + tData[1][2]*itlData[1][1] + tData[2][2]*itlData[1][2];
		float t22 = tData[0][2]*itlData[2][0] + tData[1][2]*itlData[2][1] + tData[2][2]*itlData[2][2];

		inverseInertiaTensorGlobal = new Matrix3x3(new float[][] {
				{
						t00*tData[0][0] + t10*tData[1][0] + t20*tData[2][0],
						t01*tData[0][0] + t11*tData[1][0] + t21*tData[2][0],
						t02*tData[0][0] + t12*tData[1][0] + t22*tData[2][0]
				},
				{
						t00*tData[0][1] + t10*tData[1][1] + t20*tData[2][1],
						t01*tData[0][1] + t11*tData[1][1] + t21*tData[2][1],
						t02*tData[0][1] + t12*tData[1][1] + t22*tData[2][1]
				},
				{
						t00*tData[0][2] + t10*tData[1][2] + t20*tData[2][2],
						t01*tData[0][2] + t11*tData[1][2] + t21*tData[2][2],
						t02*tData[0][2] + t12*tData[1][2] + t22*tData[2][2]
				}
		});
	}

	public void update(float tick) {
		super.update(tick);

		lastAcceleration = acceleration;
		lastAcceleration = Vector3.sum(lastAcceleration, totalForce.scaled(inverseMass));
		lastVelocity = velocity;
		velocity.add(lastAcceleration.scaled(tick));
		velocity.scale((float) Math.pow(linear_damping, tick));
		Point3 position = transformation.getPosition().offset(Vector3.sum(lastVelocity.scaled(tick), lastAcceleration.scaled(tick*tick/2)));

		Vector3 angularAcc = inverseInertiaTensorGlobal.product(totalTorque);
		rotation.add(angularAcc.scaled(tick));
		rotation.scale((float) Math.pow(angular_damping, tick));
		Quaternion orientation = transformation.getOrientation().sum(rotation.scaled(tick));

		set(position, orientation);

		transformInertiaTensor();

		clearForce();
		clearTorque();

		if (!velocity.isNull() || !rotation.isNull())
			updateInternals();
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

	public void applyForce(Vector3 force, Point3 p) {
		totalForce.add(force);
		totalTorque.add(Vector3.cross(force, Vector3.offset(transformation.getPosition(), p)));
	}

	public void applyForceLocal(Vector3 force, Point3 p) {
		applyForce(force, transformation.toGlobal(p));
	}

	public void setVelocity(Vector3 velocity) {
		this.velocity = velocity;
	}

	public void setAcceleration(Vector3 acceleration) {
		this.acceleration = acceleration;
	}

	public void setRotation(Vector3 rotation) {
		this.rotation = rotation;
	}

	public boolean isAccelerating() {
		return !acceleration.isNull();
	}

	public void stop() {
		velocity.nullify();
		acceleration.nullify();
		rotation.nullify();
	}

	public float getMass() {
		return 1/inverseMass;
	}

	public Matrix3x3 getInverseInertiaTensorGlobal() {
		return inverseInertiaTensorGlobal;
	}

	public Matrix3x3 getInverseInertiaTensorLocal() {
		return inverseInertiaTensorLocal;
	}

	public boolean isGravitated() {
		return true;
	}

	public static Vector3 getGravity() {
		return gravity;
	}

	public String getAttributesString() {
		return super.getAttributesString()+",velocity="+velocity.toString()+",acceleration="+acceleration.toString();
	}
}
