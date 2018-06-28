package engine;

import world.Point3D;
import world.Vector3D;
import world.bounding.Bounding;
import world.bounding.BoundingProperties;

public abstract class PhysicalObject extends EngineObject {

	protected Momentum momentum;
	public static float restitution = 0.4f;
	protected float mass = 0f;
	protected Vector3D nextMovement;
	protected Bounding nextBounding;
	
	public PhysicalObject(Point3D anchor, BoundingProperties boundingProperties) {
		this(anchor, boundingProperties, true);
	}
	
	public PhysicalObject(Point3D anchor, BoundingProperties boundingProperties, boolean gravitational) {
		super(anchor, boundingProperties);
		this.gravitational = gravitational;
		this.momentum = new Momentum();
	}
	
	public void update(double tick) {
		super.update(tick);
		momentum.update(tick, gravitational);

        this.bounding = new Bounding(anchor, boundingProperties);
		
		nextMovement = new Vector3D((float) (momentum.getVX()*tick), (float) (momentum.getVY()*tick), (float) (momentum.getVZ()*tick));

		if (nextMovement.equals(Vector3D.NULL_VECTOR))
			broadPhase = bounding;
		else {
			nextBounding = bounding.clone(new Point3D(anchor, nextMovement));
			broadPhase = bounding.broadPhaseWith(nextBounding);
		}
	}
	
	public void move() {
		anchor.move(nextMovement);
	}
	
	public void stop() {
		momentum.nullify();
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
				return new Collision(this, object, normal, 1f);
			}
		}
		return null;
	}
	
	public void setMomentum(Momentum momentum) {
		this.momentum = new Momentum(momentum);
	}
	
	public void addMomentum(Momentum momentum) {
		this.momentum.addMomentum(momentum);
	}
	
	public Vector3D getNextMovement() {
		return nextMovement;
	}
	
	public Momentum getMomentum() {
		return momentum;
	}
	
	public float getMass() {
		return mass;
	}
	
	public boolean isMoving() {
		return !momentum.isStill();
	}
	
	public String getAttributesString() {
		return "[x="+anchor.getX()+", y="+anchor.getY()+", z="+anchor.getZ()+", ya="+ya+", za="+za+", mmntm:"+momentum.toString()+"]";
	}
}
