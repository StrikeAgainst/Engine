package engine;

import world.Point3D;

public class Config {
	
	public static final int OCTREE_MAX_DIMENSION_EXP = 5;
	public static final int OCTREE_MAX_DEPTH = 5;
	public static Point3D ROOT = new Point3D(Point3D.ROOT);
	public static Point3D INIT_PLAYER_POS = new Point3D(0.25f,0.25f,0.5f);
}
