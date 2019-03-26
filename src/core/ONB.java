package core;

public class ONB {

    private Vector3 x, y, z;

    private ONB(Vector3 x, Vector3 y, Vector3 z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static ONB getFromXAxis(Vector3 x) {
        x = x.getNormalized();

        double xx = x.getX(), xy = x.getY(), xz = x.getZ();
        double scale, yx, yy, yz, zx, zy, zz;
        if (Math.abs(xx) > Math.abs(xy)) {
            scale = 1/Math.sqrt(xx*xx+xz*xz);
            yx = xz*scale;
            yy = 0;
            yz = -xx*scale;

            zx = xy*yx;
            zy = xz*yx-xx*yz;
            zz = -zx;
        } else {
            scale = 1/Math.sqrt(xy*xy+xz*xz);
            yx = 0;
            yy = -xz*scale;
            yz = xy*scale;

            zx = xy*yz-xz*yy;
            zy = -xx*yz;
            zz = -xx*yy;
        }

        return new ONB(x, new Vector3(yx, yy, yz), new Vector3(zx, zy, zz));
    }

    public Matrix3x3 toMatrix() {
        return new Matrix3x3(new double[][] {x.toArray(), y.toArray(), z.toArray()});
    }

    public Vector3 getX() {
        return x;
    }

    public Vector3 getY() {
        return y;
    }

    public Vector3 getZ() {
        return z;
    }
}
