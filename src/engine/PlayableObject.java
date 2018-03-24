package engine;

import world.ObjectBounding;
import world.Point3D;

public abstract class PlayableObject extends PhysicalObject {

	public static float restitution = 0f;
	public Player player;

	public PlayableObject(Point3D center, ObjectBounding bounding) {
		super(center, bounding);
	}

	public PlayableObject(Point3D center, ObjectBounding bounding, Player player) {
		this(center, bounding);
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
	
	public abstract float getCamX();
	
	public abstract float getCamY();
	
	public abstract float getCamZ();
}
