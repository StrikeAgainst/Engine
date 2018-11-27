package engine;

import world.Vector3D;

public class Physics {
    public final static boolean GRAVITY_ENABLED = true;
    public final static boolean DAMPING_ENABLED = true;
    public final static float gravity_multiplier = 1f;
    public final static Vector3D g = new Vector3D(0, 0, -2f);
    //public final static Vector3D g = new Vector3D(0, 0, -9.807f);
    public final static float damping = 0.995f;
}
