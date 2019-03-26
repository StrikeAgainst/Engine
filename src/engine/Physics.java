package engine;

import core.Vector3;

public class Physics {
    public final static Vector3 gravity = new Vector3(0,0, -9.807);
    public final static double linear_damping = 0.995, angular_damping = 0.995;
    public final static double R180 = Math.PI, R360 = 2*R180, R90 = R180/2, R45 = R180/4;

    public static double boundAngleHalf(double angle) {
        if (angle >= R90)
            angle = R90;
        else if (angle < -R90)
            angle = -R90;

        return angle;
    }

    public static double boundAngleFull(double angle) {
        if (angle >= R360)
            angle = angle % R360;
        else if (angle < 0)
            angle = R360-((-angle) % R360);

        return angle;
    }
}
