package core;

public abstract class Scalar3 {

    protected double x, y, z;

    public Scalar3() {
        this(0, 0, 0);
    }

    public Scalar3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Scalar3(double[] s) {
        this.x = s[0];
        this.y = s[1];
        this.z = s[2];
    }

    public Scalar3(Scalar3 s) {
        this.x = s.getX();
        this.y = s.getY();
        this.z = s.getZ();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Scalar3 s) {
        this.x = s.getX();
        this.y = s.getY();
        this.z = s.getZ();
    }

    public void add(Vector3 v) {
        this.x += v.getX();
        this.y += v.getY();
        this.z += v.getZ();
    }

    public void scale(double s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
    }

    public void nullify() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public double[] toArray() {
        return new double[] {x, y, z};
    }

    public boolean equals(Scalar3 s) {
        if (s == null)
            return false;
        return (this.x == s.getX() && this.y == s.getY() && this.z == s.getZ());
    }

    public String getNameString() {
        return getClass().getSimpleName();
    }

    public String getAttributesString() {
        return "x="+x+", y="+y+", z="+z;
    }

    public String toString() {
        return getNameString()+":["+getAttributesString()+"]";
    }
}
