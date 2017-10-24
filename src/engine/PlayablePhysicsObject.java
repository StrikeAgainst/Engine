package engine;

public abstract class PlayablePhysicsObject extends PhysicsObject implements Playable{
	
	public PlayablePhysicsObject(Point3D center) {
		super(center);
	}
}
