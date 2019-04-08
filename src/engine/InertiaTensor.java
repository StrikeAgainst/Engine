package engine;

import core.Matrix3x3;
import core.Vector3;

public class InertiaTensor {

    private static final Matrix3x3 immovableTensor = new Matrix3x3(new double[][] {
        {Double.POSITIVE_INFINITY,0,0},
        {0,Double.POSITIVE_INFINITY,0},
        {0,0,Double.POSITIVE_INFINITY},
    });

    private Matrix3x3 local, inverseLocal, inverseGlobal;
    private boolean immovable;

    public InertiaTensor(Matrix3x3 local) {
        this.local = new Matrix3x3(local);
        this.immovable = local.equals(immovableTensor);

        if (this.immovable){
            this.inverseLocal = Matrix3x3.getEmpty();
            this.inverseGlobal = Matrix3x3.getEmpty();
        } else
            this.inverseLocal = local.getInverse();
    }

    public InertiaTensor(double[][] data) {
        this(new Matrix3x3(data));
    }

    public static InertiaTensor createImmovable() {
        return new InertiaTensor(immovableTensor);
    }

    public void updateInverseGlobal(Transformation transformation) {
        if (!immovable) {
            Matrix3x3 orientation = transformation.getOrientationMatrix();
            inverseGlobal = orientation.product(inverseLocal).product(orientation.getTransposed());
        }
    }

    public Matrix3x3 offset(Vector3 offset, double mass) {
        if (immovable)
            return new Matrix3x3(immovableTensor);

        Matrix3x3 offsetMatrix = Matrix3x3.getIdentity(), outerProduct = Matrix3x3.getOuterProduct(offset, offset);
        offsetMatrix.scale(offset.getSquareMagnitude());
        outerProduct.scale(-1);
        offsetMatrix.add(outerProduct);
        offsetMatrix.scale(mass);
        offsetMatrix.add(inverseGlobal.getInverse());

        return offsetMatrix;
    }

    public Vector3 productInverseLocal(Vector3 v) {
        return (immovable ? new Vector3() : inverseLocal.product(v));
    }

    public Vector3 productInverseGlobal(Vector3 v) {
        return (immovable ? new Vector3() : inverseGlobal.product(v));
    }

    public Matrix3x3 productInverseLocal(Matrix3x3 m) {
        return (immovable ? Matrix3x3.getEmpty() : inverseLocal.product(m));
    }

    public Matrix3x3 productInverseGlobal(Matrix3x3 m) {
        return (immovable ? Matrix3x3.getEmpty() : inverseGlobal.product(m));
    }

    public Matrix3x3 getLocal() {
        return local;
    }

    public Matrix3x3 getInverseLocal() {
        return inverseLocal;
    }

    public Matrix3x3 getInverseGlobal() {
        if (inverseGlobal == null)
            return null;

        return inverseGlobal;
    }

    public boolean isImmovable() {
        return immovable;
    }

    public String toString() {
        return "InertiaTensor:[local:"+local.toString()+", inverseLocal:"+inverseLocal.toString()+", inverseGlobal:"+inverseGlobal.toString()+"]";
    }
}
