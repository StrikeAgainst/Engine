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
		setPlayer(player);
	}
	
	public void setPlayer(Player player) {
		this.player = player;
		if (player.getPlayerObject() != this)
			player.setPlayerObject(this);
	}
	
	public Player getPlayer() {
		return player;
	}

	public void destroy() {
		player.setPlayerObject(null);
		super.destroy();
	}
	
	public abstract float getCamX();
	
	public abstract float getCamY();
	
	public abstract float getCamZ();
}
