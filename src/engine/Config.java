package engine;

import core.Point3;

public class Config {

	public static boolean DEBUG = false;
	public static final int OCTREE_MAX_DIMENSION_EXP = 5;
	public static final int OCTREE_MAX_DEPTH = 5;
	public static Point3 ROOT = new Point3();
	public static Point3 INIT_PLAYER_POS = new Point3(0.25f,0.25f,0.5f);
}
