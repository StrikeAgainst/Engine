package core;

import java.util.Arrays;

public class Matrix3x3 extends Matrix {

    public Matrix3x3() {
        this(new float[][] {
                {1,0,0},
                {0,1,0},
                {0,0,1}
        });
    }

    public Matrix3x3(float[][] data) {
        if (data.length == 3 && data[0].length == 3)
            this.data = data;
    }

    public Point3 product(Point3 p) {
        float[] pData = p.getArray();
        return new Point3(
                data[0][0]*pData[0]+data[1][0]*pData[1]+data[2][0]*pData[2],
                data[0][1]*pData[0]+data[1][1]*pData[1]+data[2][1]*pData[2],
                data[0][2]*pData[0]+data[1][2]*pData[1]+data[2][2]*pData[2]);
    }

    public Vector3 product(Vector3 p) {
        float[] pData = p.getArray();
        return new Vector3(
                data[0][0]*pData[0]+data[1][0]*pData[1]+data[2][0]*pData[2],
                data[0][1]*pData[0]+data[1][1]*pData[1]+data[2][1]*pData[2],
                data[0][2]*pData[0]+data[1][2]*pData[1]+data[2][2]*pData[2]);
    }

    public Matrix3x3 product(Matrix3x3 m) {
        float[][] mData = m.getData();
        return new Matrix3x3(new float[][] {
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
                }
        });
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
                    data[0][0]*mData[3][0] + data[1][0]*mData[3][1] + data[2][0]*mData[3][2],
                    data[0][1]*mData[3][0] + data[1][1]*mData[3][1] + data[2][1]*mData[3][2],
                    data[0][2]*mData[3][0] + data[1][2]*mData[3][1] + data[2][2]*mData[3][2]
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
                }
        };
    }

    public Matrix3x3 getInverse() {
        return new Matrix3x3(getInverseData());
    }

    public void invert() {
        this.data = getInverseData();
    }

    public Matrix3x3 getTransposed() {
        return new Matrix3x3(new float[][] {
                {
                    data[0][0],
                    data[1][0],
                    data[2][0],
                },
                {
                    data[0][1],
                    data[1][1],
                    data[2][1],
                },
                {
                    data[0][2],
                    data[1][2],
                    data[2][2],
                }
        });
    }

    public void transpose() {
        float t;

        t = this.data[0][1];
        this.data[0][1] = this.data[1][0];
        this.data[1][0] = t;

        t = this.data[0][2];
        this.data[0][2] = this.data[2][0];
        this.data[2][0] = t;

        t = this.data[1][2];
        this.data[1][2] = this.data[2][1];
        this.data[2][1] = t;
    }

    public String toString() {
        return "Matrix3x3:["+Arrays.deepToString(data)+"]";
    }

}