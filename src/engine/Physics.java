package engine;

import core.Vector3;

public class Physics {
    public final static boolean GRAVITY_ENABLED = true;
    public final static boolean DAMPING_ENABLED = true;
    public final static float gravity_multiplier = 1f;
    public final static Vector3 g = new Vector3(0, 0, -2f);
    //public final static Vector3 g = new Vector3(0, 0, -9.807f);
    public final static float damping = 0.995f;
    public final static float R180 = (float) Math.PI, R360 = 2*R180, R90 = R180/2, R45 = R180/4;

    public static float boundAngleHalf(float angle) {
        if (angle >= R90)
            angle = R90;
        else if (angle < -R90)
            angle = -R90;

        return angle;
    }

    public static float boundAngleFull(float angle) {
        if (angle >= R360)
            angle = angle % R360;
        else if (angle < 0)
            angle = R360-((-angle) % R360);

        return angle;
    }
}
