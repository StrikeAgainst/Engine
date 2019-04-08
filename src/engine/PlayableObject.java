package engine;

import core.Point3;

public abstract class PlayableObject extends SimpleObject {

	public Player player;

	public PlayableObject(Transformation transformation, double mass) {
		super(transformation, mass);
	}

	public PlayableObject(Transformation transformation, double mass, InertiaTensor inertiaTensor) {
		super(transformation, mass, inertiaTensor);
	}

	public PlayableObject(Player player, Transformation transformation, double mass) {
		this(transformation, mass);
		attachPlayer(player);
	}

	public PlayableObject(Player player, Transformation transformation, double mass, InertiaTensor inertiaTensor) {
		this(transformation, mass, inertiaTensor);
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
