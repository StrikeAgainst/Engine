package core;

public abstract class Scalar3 {

    protected float x, y, z;

    public Scalar3() {
        this(0, 0, 0);
    }

    public Scalar3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Scalar3(float[] s) {
        this.x = s[0];
        this.y = s[1];
        this.z = s[2];
    }

    public Scalar3(Scalar3 s) {
        this.x = s.getX();
        this.y = s.getY();
        this.z = s.getZ();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
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

    public float[] toArray() {
        return new float[] {x, y, z};
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
