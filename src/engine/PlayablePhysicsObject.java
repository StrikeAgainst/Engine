package engine;

public abstract class PlayablePhysicsObject extends PhysicsObject {
	
	public PlayablePhysicsObject(float xPos, float yPos, float zPos, BoundingBox bounds) {
		super(xPos, yPos, zPos, bounds);
	}
	
	public abstract float getCamX();
	
	public abstract float getCamY();
	
	public abstract float getCamZ();

}
