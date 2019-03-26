package engine;

import core.Matrix3x3;
import core.Vector3;

public class InertiaTensor {

    private Matrix3x3 matrix, inverseLocal, inverseGlobal;

    public InertiaTensor(Matrix3x3 matrix) {
        this.matrix = matrix;
        this.inverseLocal = matrix.getInverse();
    }

    public InertiaTensor(double[][] data) {
        this(new Matrix3x3(data));
    }

    public static InertiaTensor createWithProducts(double ix, double iy, double iz) {
        double ixy = 0, ixz = 0, iyz = 0; //todo?
        return new InertiaTensor(new double[][] {
                {ix,ixy,ixz},
                {ixy,iy,iyz},
                {ixz,iyz,iz}
        });
    }

    public void updateInverseGlobal(Transformation transformation) {
        Matrix3x3 orientation = transformation.getOrientationMatrix();
        inverseGlobal = orientation.product(inverseLocal).product(orientation.getTransposed());
    }

    public Vector3 productInverseLocal(Vector3 v) {
        return inverseLocal.product(v);
    }

    public Vector3 productInverseGlobal(Vector3 v) {
        return inverseGlobal.product(v);
    }

    public Matrix3x3 productInverseLocal(Matrix3x3 m) {
        return inverseLocal.product(m);
    }

    public Matrix3x3 productInverseGlobal(Matrix3x3 m) {
        return inverseGlobal.product(m);
    }

    public Matrix3x3 getMatrix() {
        return matrix;
    }

    public Matrix3x3 getInverseLocal() {
        return inverseLocal;
    }

    public Matrix3x3 getInverseGlobal() {
        return inverseGlobal;
    }

    public String toString() {
        return "InertiaTensor:[matrix:"+matrix.toString()+", inverseLocal:"+inverseLocal.toString()+", inverseGlobal:"+inverseGlobal.toString()+"]";
    }
}
