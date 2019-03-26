package engine;

import core.Vector3;

public class InertiaTensorFactory {

    public static InertiaTensor forDefault(double mass) {
        return new InertiaTensor(new double[][] {
                {mass,0,0},
                {0,mass,0},
                {0,0,mass}
        });
    }

    public static InertiaTensor forCuboid(double mass, Vector3 size) {
        double x = size.getX(), y = size.getY(), z = size.getZ();
        double m = mass/12, x2 = x*x, y2 = y*y, z2 = z*z;
        return new InertiaTensor(new double[][] {
            {m*(y2+z2),0,0},
            {0,m*(x2+z2),0},
            {0,0,m*(x2+y2)}
        });
    }

    public static InertiaTensor forSphere(double mass, double radius, boolean hollow) {
        double i = radius*radius*mass*2/(hollow?3:5);
        return new InertiaTensor(new double[][] {
            {i,0,0},
            {0,i,0},
            {0,0,i}
        });
    }

    public static InertiaTensor forCylinder(double mass, double radius, double height) {
        double i = radius*radius*mass/2, k = i/2+height*height*mass/12;
        return new InertiaTensor(new double[][] {
            {k,0,0},
            {0,k,0},
            {0,0,i}
        });
    }

    public static InertiaTensor forCone(double mass, double radius, double height) {
        double i = radius*radius*mass*3/10, k = i/2+height*height*mass*3/80;
        return new InertiaTensor(new double[][] {
            {k,0,0},
            {0,k,0},
            {0,0,i}
        });
    }
}
