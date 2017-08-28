package engine;

import java.util.ArrayList;

public abstract class PhysicsObject extends EngineObject {

	public final static float g = -9.81f;
	
	public PhysicsObject(float xPos, float yPos, float zPos, float front, float back, float left, float right, float top, float bottom) {
		super(xPos, yPos, zPos, front, back, left, right, top, bottom);
	}
	
	public Tile onTile(double period, ArrayList<Tile> tiles) {
		float x, y, z, s;
		for (Tile t : tiles) {
			x = t.getX();
			y = t.getY();
			z = t.getZ();
			s = t.getSize();
			if (this.x-0.15f < x+s/2 && this.x+0.15f > x-s/2 && this.y-0.15f < y+s/2 && this.y+0.15f > y-s/2 && vz*period+this.z <= z && this.z >= z) return t;
		}
		return null;
	}
	
	public void move(double period, ArrayList<Tile> tiles) {
		this.vz += (float)(PhysicsObject.g*period);
		for (EngineObject e : ObjectContainer.get()) {
			if (e.crossing(this)) {
				
			}
		}
		Tile t = onTile(period, tiles);
		if (t != null) {
			this.z = t.getZ();
			this.vz = 0;
		}
		this.x += this.vx*period;
		this.y += this.vy*period;
		this.z += this.vz*period;
	}
	
	public boolean isAirborne() {
		return this.vz != 0;
	}
}
