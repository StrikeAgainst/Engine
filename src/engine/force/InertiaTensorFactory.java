package engine.force;

import core.Matrix3x3;
import core.Vector3;

public class InertiaTensorFactory {

    public static Matrix3x3 forDefault(float mass) {
        return new Matrix3x3(new float[][] {
                {mass,0,0},
                {0,mass,0},
                {0,0,mass}
        });
    }

    public static Matrix3x3 forCuboid(float mass, Vector3 size) {
        float x = size.getX(), y = size.getY(), z = size.getZ();
        float m = mass/12, x2 = x*x, y2 = y*y, z2 = z*z;
        return new Matrix3x3(new float[][] {
            {m*(y2+z2),0,0},
            {0,m*(x2+z2),0},
            {0,0,m*(x2+y2)}
        });
    }

    public static Matrix3x3 forSphere(float mass, float radius, boolean hollow) {
        float i = radius*radius*mass*2/(hollow?3:5);
        return new Matrix3x3(new float[][] {
            {i,0,0},
            {0,i,0},
            {0,0,i}
        });
    }

    public static Matrix3x3 forCylinder(float mass, float radius, float height) {
        float i = radius*radius*mass/2, k = i/2+height*height*mass/12;
        return new Matrix3x3(new float[][] {
            {k,0,0},
            {0,k,0},
            {0,0,i}
        });
    }

    public static Matrix3x3 forCone(float mass, float radius, float height) {
        float i = radius*radius*mass*3/10, k = i/2+height*height*mass*3/80;
        return new Matrix3x3(new float[][] {
            {k,0,0},
            {0,k,0},
            {0,0,i}
        });
    }
}
