package core;

import java.util.Arrays;

public class Matrix3x4 extends Matrix {

    public Matrix3x4(float[][] data) {
        if (data.length == 4 && data[0].length == 3)
            this.data = data;
    }

    public float[][] getHomogenData() {
        float[][] data = new float[4][];

        for (int i = 0; i < 4; i++) {
            data[i] = new float[4];
            for (int j = 0; j < 3; j++)
                data[i][j] = this.data[i][j];

            data[i][3] = (i == 3?1:0);
        }

        return data;
    }

    public float[] getDataLinear(boolean byRows) {
        return getDataLinear(byRows, false);
    }

    public float[] getDataLinear(boolean byRows, boolean homogen) {
        float[][] data = (homogen?getHomogenData():this.data);
        int index = 0, columns = data.length, rows = data[0].length;
        float[] arr = new float[columns*rows];

        if (byRows)
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < columns; j++)
                    arr[index++] = data[j][i];
        else
            for (int i = 0; i < columns; i++)
                for (int j = 0; j < rows; j++)
                    arr[index++] = data[i][j];

        return arr;
    }

    public Point3 product(Point3 p) {
        float[] pData = p.getArray();
        return new Point3(
                data[0][0]*pData[0]+data[1][0]*pData[1]+data[2][0]*pData[2]+data[3][0],
                data[0][1]*pData[0]+data[1][1]*pData[1]+data[2][1]*pData[2]+data[3][1],
                data[0][2]*pData[0]+data[1][2]*pData[1]+data[2][2]*pData[2]+data[3][2]);
    }

    public Point3 productInverse(Point3 p) {
        float[] pData = p.getArray();
        pData[0] -= data[3][0];
        pData[1] -= data[3][1];
        pData[2] -= data[3][2];
        return new Point3(
                data[0][0]*pData[0]+data[0][1]*pData[1]+data[0][2]*pData[2],
                data[1][0]*pData[0]+data[1][1]*pData[1]+data[1][2]*pData[2],
                data[2][0]*pData[0]+data[2][1]*pData[1]+data[2][2]*pData[2]);
    }

    public Vector3 product(Vector3 v) {
        float[] vData = v.getArray();
        return new Vector3(
                data[0][0]*vData[0]+data[1][0]*vData[1]+data[2][0]*vData[2],
                data[0][1]*vData[0]+data[1][1]*vData[1]+data[2][1]*vData[2],
                data[0][2]*vData[0]+data[1][2]*vData[1]+data[2][2]*vData[2]);
    }

    public Vector3 productInverse(Vector3 v) {
        float[] vData = v.getArray();
        return new Vector3(
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
        this.data = getInverseData();
    }

    public String toString() {
        return toString(false);
    }

    public String toString(boolean homogen) {
        return "Matrix3x4:["+ Arrays.deepToString(homogen?getHomogenData():data)+"]";
    }
}
