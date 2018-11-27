package world;

public class Matrix3x4 extends Matrix3D {

    public Matrix3x4(float[][] data) {
        if (data.length == 4 && data[0].length == 3)
            this.data = data;
    }

    public Matrix3x4(Point3D p, Quaternion q) {
        float r = q.getR(), i = q.getI(), j = q.getJ(), k = q.getK();
        float x = p.getX(), y = p.getY(), z = p.getZ();

        this.data[0][0] = 1 - (2*j*j + 2*k*k);
        this.data[0][1] = 2*i*j + 2*k*r;
        this.data[0][2] = 2*i*k + 2*j*r;

        this.data[1][0] = 2*i*j + 2*k*r;
        this.data[1][1] = 1 - (2*i*i + 2*k*k);
        this.data[1][2] = 2*j*k + 2*i*r;

        this.data[2][0] = 2*i*k + 2*j*r;
        this.data[2][1] = 2*j*k + 2*i*r;
        this.data[2][2] = 1 - (2*i*i + 2*j*j);

        this.data[3][0] = x;
        this.data[3][1] = y;
        this.data[3][2] = z;
    }

    public Point3D product(Point3D p) {
        float[] pData = p.getArray();
        return new Point3D(
                data[0][0]*pData[0]+data[1][0]*pData[1]+data[2][0]*pData[2]+data[3][0],
                data[0][1]*pData[0]+data[1][1]*pData[1]+data[2][1]*pData[2]+data[3][1],
                data[0][2]*pData[0]+data[1][2]*pData[1]+data[2][2]*pData[2]+data[3][2]);
    }

    public Point3D productInverse(Point3D p) {
        float[] pData = p.getArray();
        pData[0] -= data[3][0];
        pData[1] -= data[3][1];
        pData[2] -= data[3][2];
        return new Point3D(
                data[0][0]*pData[0]+data[0][1]*pData[1]+data[0][2]*pData[2],
                data[1][0]*pData[0]+data[1][1]*pData[1]+data[1][2]*pData[2],
                data[2][0]*pData[0]+data[2][1]*pData[1]+data[2][2]*pData[2]);
    }

    public Vector3D product(Vector3D v) {
        float[] vData = v.getArray();
        return new Vector3D(
                data[0][0]*vData[0]+data[1][0]*vData[1]+data[2][0]*vData[2],
                data[0][1]*vData[0]+data[1][1]*vData[1]+data[2][1]*vData[2],
                data[0][2]*vData[0]+data[1][2]*vData[1]+data[2][2]*vData[2]);
    }

    public Vector3D productInverse(Vector3D v) {
        float[] vData = v.getArray();
        return new Vector3D(
                data[0][0]*vData[0]+data[0][1]*vData[1]+data[0][2]*vData[2],
                data[1][0]*vData[0]+data[1][1]*vData[1]+data[1][2]*vData[2],
                data[2][0]*vData[0]+data[2][1]*vData[1]+data[2][2]*vData[2]);
    }

    public Matrix3x4 product(Matrix3x4 m) {
        float[][] mData = m.getData();
        return new Matrix3x4(new float[][] {
                {
                    data[0][0]*mData[0][0] + data[1][0]*mData[0][1] + data[2][0]*mData[0][2],
                    data[0][1]*mData[0][0] + data[1][1]*mData[0][1] + data[2][1]*mData[0][2],
                    data[0][2]*mData[0][0] + data[1][2]*mData[0][1] + data[2][2]*mData[0][2]
                },
                {
                    data[0][0]*mData[1][0] + data[1][0]*mData[1][1] + data[2][0]*mData[1][2],
                    data[0][1]*mData[1][0] + data[1][1]*mData[1][1] + data[2][1]*mData[1][2],
                    data[0][2]*mData[1][0] + data[1][2]*mData[1][1] + data[2][2]*mData[1][2]
                },
                {
                    data[0][0]*mData[2][0] + data[1][0]*mData[2][1] + data[2][0]*mData[2][2],
                    data[0][1]*mData[2][0] + data[1][1]*mData[2][1] + data[2][1]*mData[2][2],
                    data[0][2]*mData[2][0] + data[1][2]*mData[2][1] + data[2][2]*mData[2][2]
                },
                {
                    data[0][0]*mData[3][0] + data[1][0]*mData[3][1] + data[2][0]*mData[3][2] + data[3][0],
                    data[0][1]*mData[3][0] + data[1][1]*mData[3][1] + data[2][1]*mData[3][2] + data[3][1],
                    data[0][2]*mData[3][0] + data[1][2]*mData[3][1] + data[2][2]*mData[3][2] + data[3][2]
                }
        });
    }

    public float getDeterminant() {
        return data[0][0]*data[1][1]*data[2][2]
              + data[0][1]*data[1][2]*data[2][0]
              + data[0][2]*data[1][0]*data[2][1]
              - data[0][0]*data[1][2]*data[2][1]
              - data[0][1]*data[1][0]*data[2][2]
              - data[0][2]*data[1][1]*data[2][0];
    }

    public float[][] getInverseData() {
        float det = getDeterminant();
        if (det == 0)
            return null;

        return new float[][] {
                {
                    (data[1][1]*data[2][2] - data[1][2]*data[2][1])*det,
                    (data[1][2]*data[2][0] - data[1][0]*data[2][2])*det,
                    (data[1][0]*data[2][1] - data[1][1]*data[2][0])*det
                },
                {
                    (data[0][2]*data[2][1] - data[0][1]*data[2][2])*det,
                    (data[0][0]*data[2][2] - data[0][2]*data[2][0])*det,
                    (data[0][1]*data[2][0] - data[0][0]*data[2][1])*det
                },
                {
                    (data[0][1]*data[1][2] - data[0][2]*data[1][1])*det,
                    (data[0][2]*data[1][0] - data[0][0]*data[1][2])*det,
                    (data[0][0]*data[1][1] - data[0][1]*data[1][0])*det
                },
                {
                    (data[1][2]*data[2][1]*data[3][0] + data[1][0]*data[2][2]*data[3][1] + data[1][1]*data[2][0]*data[3][2] -
                     data[1][0]*data[2][1]*data[3][2] - data[1][1]*data[2][2]*data[3][0] - data[1][2]*data[2][0]*data[3][1])*det,
                    (data[0][2]*data[2][1]*data[3][0] + data[0][0]*data[2][2]*data[3][1] + data[0][1]*data[2][0]*data[3][2] -
                     data[0][0]*data[2][1]*data[3][2] - data[0][1]*data[2][2]*data[3][0] - data[0][2]*data[2][0]*data[3][1])*det,
                    (data[0][2]*data[1][1]*data[3][0] + data[0][0]*data[1][2]*data[3][1] + data[0][1]*data[1][0]*data[3][2] -
                     data[0][0]*data[1][1]*data[3][2] - data[0][1]*data[1][2]*data[3][0] - data[0][2]*data[1][0]*data[3][1])*det
                }
        };
    }

    public Matrix3x4 getInverse() {
        return new Matrix3x4(getInverseData());
    }

    public void invert() {
        this.data = getInverse().getData();
    }
}
