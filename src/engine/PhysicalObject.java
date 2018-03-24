package engine;

import world.BoundingBox;
import world.ObjectBounding;
import world.Point3D;
import world.Vector3D;

public abstract class PhysicalObject extends EngineObject {

	protected Momentum momentum;
	public final static float g = -9.81f;
	public static float restitution = 0.4f;
	protected float mass = 0f;
	protected Vector3D nextMovement;
	
	public PhysicalObject(Point3D anchor, ObjectBounding bounding) {
		this(anchor, bounding, true);		
	}
	
	public PhysicalObject(Point3D center, ObjectBounding bounding, boolean gravitational) {
		super(center, bounding);
		this.gravitational = gravitational;
		this.momentum = new Momentum();
	}
	
	public void update(double tick) {
		super.update(tick);
		momentum.update(tick, gravitational);
		
		nextMovement = new Vector3D((float) (momentum.getVX()*tick), (float) (momentum.getVY()*tick), (float) (momentum.getVZ()*tick));
		
		if (nextMovement.equals(Vector3D.NULL_VECTOR)) 
			broadPhase = bounding.boxify();
		else {
			ObjectBounding nextBounding = bounding.clone();
			nextBounding.getAnchor().move(nextMovement);
			
			float mx = nextMovement.getX(), my = nextMovement.getY(), mz = nextMovement.getZ();
			float broadFront, broadBack, broadLeft, broadRight, broadTop, broadBottom;
			if (mx > 0) {
				broadFront = nextBounding.getFrontBound(true);
				broadBack = bounding.getBackBound(true);
			} else {
				broadFront = bounding.getFrontBound(true);
				broadBack = nextBounding.getBackBound(true);
			}
			if (my > 0) {
				broadLeft = nextBounding.getLeftBound(true);
				broadRight = bounding.getRightBound(true);
			} else {
				broadLeft = bounding.getLeftBound(true);
				broadRight = nextBounding.getRightBound(true);
			}
			if (mz > 0) {
				broadTop = nextBounding.getTopBound(true);
				broadBottom = bounding.getBottomBound(true);
			} else {
				broadTop = bounding.getTopBound(true);
				broadBottom = nextBounding.getBottomBound(true);
			}
	
			broadPhase = new BoundingBox(anchor, broadFront, broadBack, broadLeft, broadRight, broadTop, broadBottom);
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
		if (broadPhase == null || broadPhase.intersects(object.getBroadPhase()) != null) {
			ObjectBounding otherBounding = object.getBounding();
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
		this.momentum = momentum.clone();
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
