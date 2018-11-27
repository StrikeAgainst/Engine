package engine;

import world.Point3D;
import world.bounding.BoundingProperties;

public abstract class PlayableObject extends PhysicalObject {

	public static float restitution = 0f;
	public Player player;

	public PlayableObject(Point3D anchor, BoundingProperties boundingProperties) {
		super(anchor, boundingProperties);
	}

	public PlayableObject(Point3D anchor, BoundingProperties boundingProperties, Player player) {
		this(anchor, boundingProperties);
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
	
	public abstract float getCameraX();
	
	public abstract float getCameraY();
	
	public abstract float getCameraZ();
}
