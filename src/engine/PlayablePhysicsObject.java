package engine;

public abstract class PlayablePhysicsObject extends PhysicsObject {
	
	public PlayablePhysicsObject(float xPos, float yPos, float zPos, float front, float back, float left, float right, float top, float bottom) {
		super(xPos, yPos, zPos, front, back, left, right, top, bottom);
	}
	
	public abstract float getCamX();
	
	public abstract float getCamY();
	
	public abstract float getCamZ();

}
