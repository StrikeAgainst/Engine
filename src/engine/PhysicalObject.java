package engine;

import engine.collision.BroadPhaseContact;
import engine.collision.Collision;
import core.*;
import engine.collision.PrimitiveContact;
import engine.collision.bounding.BroadPhase;
import engine.force.InertiaTensorFactory;

import java.util.ArrayList;

public abstract class PhysicalObject extends RigidObject {

	protected static float linear_damping = 0.995f, angular_damping = 0.995f;
	protected static Vector3 gravity = new Vector3(0,0, -9.807f);

	protected Matrix3x3 inverseInertiaTensorLocal, inverseInertiaTensorWorld;

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

		inverseInertiaTensorWorld = new Matrix3x3(new float[][] {
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

		Vector3 acc = Vector3.sum(acceleration, totalForce.scaled(inverseMass));
		velocity.add(acc.scaled(tick));
		velocity.scale((float) Math.pow(linear_damping, tick));
		Point3 position = transformation.getPosition().offset(velocity.scaled(tick));

		Vector3 angular_acc = inverseInertiaTensorWorld.product(totalTorque);
		rotation.add(angular_acc.scaled(tick));
		rotation.scale((float) Math.pow(angular_damping, tick));
		Quaternion orientation = transformation.getOrientation().sum(rotation.scaled(tick));

		transformation.set(position, orientation);

		transformInertiaTensor();

		clearForce();
		clearTorque();

		if (!velocity.isNull() || !rotation.isNull())
			updateInternals();
	}
	
	public Collision collides(RigidObject object) {
		if (bounding == null || object.getBounding() == null)
			return null;

		BroadPhaseContact broadphaseContact = bounding.broadphaseContactWith(object.getBounding());
		if (broadphaseContact == null)
			return null;

		ArrayList<PrimitiveContact> contacts = bounding.contactsWith(object.getBounding());
		return new Collision(this, object, contacts, broadphaseContact);
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

	public float getInverseMass() {
		return inverseMass;
	}

	public boolean isGravitated() {
		return true;
	}

	public static Vector3 getGravity() {
		return gravity;
	}

	public BroadPhase getSweptBroadPhase() {
		// todo
		return null;
	}

	public String getAttributesString() {
		return super.getAttributesString()+",velocity="+velocity.toString()+",acceleration="+acceleration.toString();
	}
}
