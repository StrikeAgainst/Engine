package engine;

import core.Matrix3x4;
import core.Point3;
import core.Quaternion;

public class Transformation {

    private Point3 position;
    private Quaternion orientation;
    private Matrix3x4 matrix;

    public Transformation() {
        this(new Point3(), new Quaternion());
        matrix = new Matrix3x4(new float[][] {
                {1,0,0},
                {0,1,0},
                {0,0,1},
                {0,0,0}
        });
    }

    public Transformation(Point3 position, Quaternion orientation) {
        this.position = position;
        this.orientation = orientation;
        update();
    }

    public Transformation(Point3 position) {
        this(position, new Quaternion());
    }

    public Transformation(Quaternion orientation) {
        this(new Point3(), orientation);
    }

    public static Matrix3x4 calculate(Point3 position, Quaternion orientation) {
        float x = position.getX(), y = position.getY(), z = position.getZ();
        float r = orientation.getR(), i = orientation.getI(), j = orientation.getJ(), k = orientation.getK();
        float ir2 = 2*i*r, ii2 = 2*i*i, ij2 = 2*i*j, ik2 = 2*i*k, jr2 = 2*j*r, jj2 = 2*j*j, jk2 = 2*j*k, kr2 = 2*k*r, kk2 = 2*k*k;
        float[][] data = new float[][] {
                {1 - (jj2 + kk2),   ij2 + kr2,          ik2 - jr2},
                {ij2 - kr2,         1 - (ii2 + kk2),    jk2 + ir2},
                {ik2 + jr2,         jk2 - ir2,          1 - (ii2 + jj2)},
                {x,                 y,                  z}
        };

        return new Matrix3x4(data);
    }

    public void update() {
        matrix = calculate(position, orientation);
    }

    public static Transformation combine(Transformation[] transformations) {
        int length = transformations.length;
        Point3 position = transformations[length-1].getPosition();
        Quaternion orientation = transformations[length-1].getOrientation();

        for (int i = length-2; i >= 0; i--) {
            position = transformations[i].toGlobal(position);
            orientation = transformations[i].getOrientation().product(orientation);
        }

        return new Transformation(position, orientation);
    }

    public static Transformation combine(Transformation t1, Transformation t2) {
        Point3 position = t1.toGlobal(t2.getPosition());
        Quaternion orientation = t1.getOrientation().product(t2.getOrientation());

        return new Transformation(position, orientation);
    }

    public Point3 toGlobal(Point3 p) {
        return matrix.product(p);
    }

    public Point3 toLocal(Point3 p) {
        return matrix.productInverse(p);
    }

    public Point3 getPosition() {
        return position;
    }

    public Quaternion getOrientation() {
        return orientation;
    }

    public void set(Point3 p, Quaternion q) {
        boolean update = false;
        if (p != null && !position.equals(p)) {
            position.set(p);
            update = true;
        }

        if (q != null) {
            q = q.getNormalized();
            if (!orientation.equals(q)) {
                orientation.set(q);
                update = true;
            }
        }

        if (update)
            update();
    }

    public void setPosition(Point3 p) {
        if (p != null && !position.equals(p)) {
            position.set(p);
            update();
        }
    }

    public void setOrientation(Quaternion q) {
        if (q != null) {
            q = q.getNormalized();
            if (!orientation.equals(q)) {
                orientation.set(q);
                update();
            }
        }
    }

    public Matrix3x4 getMatrix() {
        return matrix;
    }

    public String toString() {
        return "Transformation:[Matrix:"+ matrix.toString(false)+"]";
    }
}
