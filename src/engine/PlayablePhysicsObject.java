package engine;

import world.ObjectBounding;
import world.Point3D;

public abstract class PlayablePhysicsObject extends PhysicsObject implements Playable {

	public static float restitution = 0f;

	public PlayablePhysicsObject(Point3D center, ObjectBounding bounding) {
		super(center, bounding);
	}
}
