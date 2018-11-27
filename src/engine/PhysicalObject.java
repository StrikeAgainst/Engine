package engine;

import engine.collision.Collision;
import engine.collision.CollisionFactory;
import engine.collision.CollisionFactoryException;
import world.Point3D;
import world.Vector3D;
import world.bounding.Bounding;
import world.bounding.BoundingProperties;

public abstract class PhysicalObject extends EngineObject {

	protected float inverse_mass = 1f;
	protected Vector3D velocity = new Vector3D(), acceleration = new Vector3D();
	protected Vector3D nextMovement;
	protected Bounding nextBounding;
	protected float vya = 0, vza = 0;
	
	public PhysicalObject(Point3D anchor, BoundingProperties boundingProperties) {
		this(anchor, boundingProperties, true);
	}
	
	public PhysicalObject(Point3D anchor, BoundingProperties boundingProperties, boolean gravitational) {
		super(anchor, boundingProperties);
		this.gravitational = gravitational;
	}
	
	public void update(float tick) {
		super.update(tick);

		if (Physics.GRAVITY_ENABLED && gravitational)
			velocity.add(Vector3D.product(Physics.g, Physics.gravity_multiplier*tick));
		if (Physics.DAMPING_ENABLED)
			velocity.stretch(Physics.damping);

        this.bounding = new Bounding(anchor, bounding.getProperties());
		
		nextMovement = Vector3D.product(velocity, tick);

		if (isStill())
			broadPhase = bounding;
		else {
			nextBounding = bounding.clone(new Point3D(anchor, velocity));
			broadPhase = bounding.broadPhaseWith(nextBounding);
		}
	}
	
	public void toggleGravitational() {
		gravitational = !gravitational;
	}
	
	public Collision collides(EngineObject object) {
		if (broadPhase.intersects(object.getBroadPhase()) != null) {
			Bounding otherBounding = object.getBounding();
			Vector3D normal = bounding.intersects(otherBounding);
			if (normal != null) {
				this.highlight();
				object.highlight();
				try {
					return CollisionFactory.createCollision(this, object, 1f);
				} catch (CollisionFactoryException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return null;
	}

	public void move() {
		anchor.move(nextMovement);
	}

	public void stop() {
		velocity.set(0, 0, 0);
		this.vya = 0;
		this.vza = 0;
	}

	public float getMass() {
		return 1/inverse_mass;
	}
	
	public float getInverseMass() {
		return inverse_mass;
	}

	public Vector3D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3D velocity) {
		this.velocity = velocity;
	}

	public boolean isStill() {
		return velocity.isNull();
	}
	
	public String getAttributesString() {
		return "[position="+anchor.toString()+", ya="+ya+", za="+za+", velocity:"+velocity.toString()+", acceleration:"+acceleration.toString()+"]";
	}
}
