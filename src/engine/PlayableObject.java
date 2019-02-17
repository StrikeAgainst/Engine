package engine;

import core.Matrix3x3;
import core.Point3;
import core.Quaternion;

public abstract class PlayableObject extends PhysicalObject {

	protected static float restitution = 0f;
	public Player player;

	public PlayableObject(Point3 position, Quaternion orientation, float mass) {
		super(position, orientation, mass);
	}

	public PlayableObject(Point3 position, Quaternion orientation, float mass, Matrix3x3 inertiaTensor) {
		super(position, orientation, mass, inertiaTensor);
	}

	public PlayableObject(Player player, Point3 position, Quaternion orientation, float mass) {
		this(position, orientation, mass);
		attachPlayer(player);
	}

	public PlayableObject(Player player, Point3 position, Quaternion orientation, float mass, Matrix3x3 inertiaTensor) {
		this(position, orientation, mass, inertiaTensor);
		attachPlayer(player);
	}
	
	public void attachPlayer(Player player) {
		if (player != null) {
			this.player = player;
			if (player.getPlayerObject() != this)
				player.attachPlayerObject(this);
		}
	}

	public void detachPlayer() {
		Player detached = this.player;
		this.player = null;
		if (detached.getPlayerObject() == this)
			detached.detachPlayerObject();
	}
	
	public Player getPlayer() {
		return player;
	}

	public void destroy() {
		player.attachPlayerObject(null);
		super.destroy();
	}

	public Point3 getCameraPosition() {
		return transformation.getPosition();
	}
}
