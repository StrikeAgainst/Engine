package engine;

public abstract class PhysicsObject extends EngineObject {

	public final static float g = -9.81f;
	boolean canMove = true;
	
	public PhysicsObject(Point3D center) {
		super(center);
	}
	
	public void move(double tick) {
		this.vz += (float)(PhysicsObject.g*tick);
		canMove = true;
		for (EngineObject e : ObjectContainer.get()) {
			if (bounds.intersect(e.getBounds())) {
				System.out.println("Can't move!");
				canMove = false;
			}
		}
		if (!canMove) {
			this.vx = 0;
			this.vy = 0;
			this.vz = 0;
		}
		center.move((float) (this.vx*tick), (float) (this.vy*tick), (float) (this.vz*tick));
	}
	
	public boolean isAirborne() {
		return this.vz != 0;
	}
	
	public String toString() {
		return "[x="+center.getX()+", y="+center.getY()+", z="+center.getZ()+", vx="+vx+", vy="+vy+", vz="+vz+", ya="+ya+", za="+za+"]";
	}
}
