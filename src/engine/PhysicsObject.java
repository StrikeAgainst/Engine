package engine;

import world.ObjectBounding;
import world.Point3D;
import world.Vector3D;

public abstract class PhysicsObject extends EngineObject {

	protected float vx = 0, vy = 0, vz = 0, ya = 0, za = 60;
	public final static float g = -9.81f;
	public static float restitution = 0.4f;
	
	public PhysicsObject(Point3D anchor, ObjectBounding bounding) {
		super(anchor, bounding);
		gravitational = true;
	}
	
	public PhysicsObject(Point3D center, ObjectBounding bounding, boolean gravitational) {
		super(center, bounding);
		this.gravitational = gravitational;
	}
	
	public void move(double tick) {
		if (gravitational)
			this.vz += (float)(PhysicsObject.g*tick);

		moving = (this.vx != 0 || this.vy != 0 || this.vz != 0);
		if (moving)
			anchor.move((float) (this.vx*tick), (float) (this.vy*tick), (float) (this.vz*tick));
	}
	
	public void stop() {
		this.vx = 0;
		this.vy = 0;
		this.vz = 0;
	}
	
	public boolean isAirborne() {
		return this.vz != 0;
	}
	
	public void toggleGravitational() {
		gravitational = !gravitational;
	}
	
	public boolean handleIntersections() {
		for (Intersection intersection : intersections) {

		}
		return true;
	};
	
	public Vector3D intersects(EngineObject object){
		return bounding.intersects(object.getBounding());
	}
	
	public float getVX() {
		return vx;
	}
	
	public void setVX(float vx) {
		this.vx = vx;
	}
	
	public float getVY() {
		return vy;
	}
	
	public void setVY(float vy) {
		this.vy = vy;
	}
	
	public float getVZ() {
		return vz;
	}
	
	public void setVZ(float vz) {
		this.vz = vz;
	}
	
	public float getYAngle() {
		return ya;
	}
	
	public void setYAngle(float ya) {
		this.ya = ya;
	}
	
	public float getZAngle() {
		return za; 
	}
	
	public void setZAngle(float za) {
		this.za = za;
	}
	
	public String toString() {
		return id+":[x="+anchor.getX()+", y="+anchor.getY()+", z="+anchor.getZ()+", vx="+vx+", vy="+vy+", vz="+vz+", ya="+ya+", za="+za+"]";
	}
}
