package core;

public class Quaternion {

    private float r, i, j, k;

    public Quaternion() {
        this(1, 0, 0, 0);
    }

    public Quaternion(float r, float i, float j, float k) {
        this.r = r;
        this.i = i;
        this.j = j;
        this.k = k;
    }

    public Quaternion(Quaternion q) {
        this.r = q.getR();
        this.i = q.getI();
        this.j = q.getJ();
        this.k = q.getK();
    }

    public static Quaternion fromYaw(float yaw) {
        float yaw2 = yaw/2;
        float sinYaw = (float) Math.sin(yaw2), cosYaw = (float) Math.cos(yaw2);
        return new Quaternion(cosYaw, 0, 0, sinYaw);
    }

    public static Quaternion fromYawAndPitch(float yaw, float pitch) {
        float yaw2 = yaw/2, pitch2 = pitch/2;
        float sinYaw = (float) Math.sin(yaw2), cosYaw = (float) Math.cos(yaw2), sinPitch = (float) Math.sin(pitch2), cosPitch = (float) Math.cos(pitch2);
        return new Quaternion(cosYaw*cosPitch, -(sinYaw*sinPitch), cosYaw*sinPitch, sinYaw*cosPitch);
    }

    public float getR() {
        return r;
    }

    public float getI() {
        return i;
    }

    public float getJ() {
        return j;
    }

    public float getK() {
        return k;
    }

    public void setR(float r) {
        this.r = r;
    }

    public void setI(float i) {
        this.i = i;
    }

    public void setJ(float j) {
        this.j = j;
    }

    public void setK(float k) {
        this.k = k;
    }

    public void set(float r, float i, float j, float k) {
        this.r = r;
        this.i = i;
        this.j = j;
        this.k = k;
    }

    public void set(Quaternion q) {
        this.r = q.getR();
        this.i = q.getI();
        this.j = q.getJ();
        this.k = q.getK();
    }

    public void normalize() {
        set(getNormalized());
    }

    public Quaternion getNormalized() {
        float d = r*r+i*i+j*j+k*k;

        if (d == 0)
            return new Quaternion();
        else if (d != 1) {
            d = (float) (1/Math.sqrt(d));
            return new Quaternion(r*d, i*d, j*d, k*d);
        }

        return new Quaternion(this);
    }

    public void invert() {
        i = -i;
        j = -j;
        k = -k;
    }

    public Quaternion getInverse() {
        return new Quaternion(r, -i, -j, -k);
    }

    public Quaternion sum(Vector3 v) {
        Quaternion q = product(new Quaternion(0, v.getX(), v.getY(), v.getZ()));
        return new Quaternion(r + q.getR()*0.5f, i + q.getI()*0.5f, j + q.getJ()*0.5f, k + q.getK()*0.5f);
    }

    public void add(Vector3 v) {
        Quaternion q = sum(v);
        r = q.getR();
        i = q.getI();
        j = q.getJ();
        k = q.getK();
    }

    public void multiply(Quaternion q) {
        q = product(q);
        r = q.getR();
        i = q.getI();
        j = q.getJ();
        k = q.getK();
    }

    public Quaternion product(Quaternion q) {
        float qr = q.getR(), qi = q.getI(), qj = q.getJ(), qk = q.getK();
        return new Quaternion(
                r*qr - i*qi - j*qj - k*qk,
                r*qi + i*qr + j*qk - k*qj,
                r*qj + j*qr + k*qi - i*qk,
                r*qk + k*qr + i*qj - j*qi);
    }

    public Quaternion getTwist(Vector3 axis) {
        if (inSingularity())
            return new Quaternion(this);

        Vector3 rot = new Vector3(i, j, k);
        Vector3 pro = rot.projection(axis);
        Quaternion twist = new Quaternion(r, pro.getX(), pro.getY(), pro.getZ());
        twist.normalize();
        return twist;
    }

    public Quaternion getSwing(Vector3 axis) {
        if (inSingularity())
            return new Quaternion();

        return product(getTwist(axis).getInverse());
    }

    public Quaternion getSwing(Quaternion twist) {
        if (inSingularity())
            return new Quaternion();

        twist.normalize();
        return product(twist.getInverse());
    }

    public Quaternion getYawTwist() {
        if (inSingularity())
            return new Quaternion(this);

        Quaternion twist = new Quaternion(r, 0, 0, k);
        twist.normalize();
        return twist;
    }

    public boolean inSingularity() {
        return (r == 1 || r == -1);
    }

    public float toAngle() {
        return (float) (2*Math.acos(r));
    }

    public AxisAngle toAxisAngle() {
        float x = 1, y = 1, z = 1;

        if (!inSingularity()) {
            float s = (r == 0?1:(float) Math.sqrt(1-r*r));
            x = i/s;
            y = j/s;
            z = k/s;
        }

        return new AxisAngle(new Vector3(x, y, z), toAngle());
    }

    public boolean equals(Quaternion q) {
        if (q == null)
            return false;
        return (this.r == q.getR() && this.i == q.getI() && this.j == q.getJ() && this.k == q.getK());
    }

    public String toString() {
        return "Quaternion:[r="+r+",i="+i+",j="+j+",k="+k+"]";
    }
}
