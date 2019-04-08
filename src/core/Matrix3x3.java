package core;

import java.util.Arrays;

public class Matrix3x3 extends Matrix {

    public Matrix3x3(double[][] data) {
        if (data.length == 3 && data[0].length == 3)
            this.data = data;
    }

    public Matrix3x3(Matrix3x3 m) {
        this.data = m.getData();
    }

    public static Matrix3x3 getIdentity() {
        return new Matrix3x3(new double[][] {
                {1,0,0},
                {0,1,0},
                {0,0,1}
        });
    }

    public static Matrix3x3 getEmpty() {
        return new Matrix3x3(new double[][] {
                {0,0,0},
                {0,0,0},
                {0,0,0}
        });
    }

    public static Matrix3x3 getSkewSymmetric(Scalar3 s) {
        double x = s.getX(), y = s.getY(), z = s.getZ();
        return new Matrix3x3(new double[][] {
                { 0, z,-y},
                {-z, 0, x},
                { y,-x, 0}
        });
    }

    public static Matrix3x3 getOuterProduct(Vector3 v, Vector3 w) {
        double vx = v.getX(), vy = v.getY(), vz = v.getZ(), wx = w.getX(), wy = w.getY(), wz = w.getZ();
        return new Matrix3x3(new double[][] {
                {vx*wx,vy*wx,vz*wx},
                {vx*wy,vy*wy,vz*wy},
                {vx*wz,vy*wz,vz*wz}
        });
    }

    public Matrix3x3 sum(Matrix3x3 m) {
        double[][] mData = m.getData();
        return new Matrix3x3(new double[][] {
                {
                        data[0][0]+mData[0][0],
                        data[0][1]+mData[0][1],
                        data[0][2]+mData[0][2]
                },
                {
                        data[1][0]+mData[1][0],
                        data[1][1]+mData[1][1],
                        data[1][2]+mData[1][2]
                },
                {
                        data[2][0]+mData[2][0],
                        data[2][1]+mData[2][1],
                        data[2][2]+mData[2][2]
                }
        });
    }

    public void add(Matrix3x3 m) {
        data = sum(m).getData();
    }

    public Point3 product(Point3 p) {
        double[] pData = p.toArray();
        return new Point3(
                data[0][0]*pData[0]+data[1][0]*pData[1]+data[2][0]*pData[2],
                data[0][1]*pData[0]+data[1][1]*pData[1]+data[2][1]*pData[2],
                data[0][2]*pData[0]+data[1][2]*pData[1]+data[2][2]*pData[2]);
    }

    public Vector3 product(Vector3 p) {
        double[] pData = p.toArray();
        return new Vector3(
                data[0][0]*pData[0]+data[1][0]*pData[1]+data[2][0]*pData[2],
                data[0][1]*pData[0]+data[1][1]*pData[1]+data[2][1]*pData[2],
                data[0][2]*pData[0]+data[1][2]*pData[1]+data[2][2]*pData[2]);
    }

    public Matrix3x3 product(Matrix3x3 m) {
        double[][] mData = m.getData();
        return new Matrix3x3(new double[][] {
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

    public void multiply(Matrix3x3 m) {
        data = product(m).getData();
    }

    public Matrix3x4 product(Matrix3x4 m) {
        double[][] mData = m.getData();
        return new Matrix3x4(new double[][] {
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

    public double getDeterminant() {
        return data[0][0]*data[1][1]*data[2][2]
             + data[0][1]*data[1][2]*data[2][0]
             + data[0][2]*data[1][0]*data[2][1]
             - data[0][0]*data[1][2]*data[2][1]
             - data[0][1]*data[1][0]*data[2][2]
             - data[0][2]*data[1][1]*data[2][0];
    }

    public double[][] getInverseData() {
        double det = getDeterminant();
        if (det == 0) {
            System.out.println("Determinant equals zero! " + toString());
            System.exit(0);
        }

        det = 1/det;

        return new double[][] {
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
        return new Matrix3x3(new double[][] {
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
        double t;

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

    public class MatrixException extends Exception {

        public MatrixException(String message) {
            super(message);
        }
    }

}
