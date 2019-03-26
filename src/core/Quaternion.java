package core;

public class Quaternion {

    private double r, i, j, k;

    public Quaternion() {
        this(1, 0, 0, 0);
    }

    public Quaternion(double r, double i, double j, double k) {
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

    public static Quaternion fromYaw(double yaw) {
        double yaw2 = yaw/2;
        double sinYaw = Math.sin(yaw2), cosYaw = Math.cos(yaw2);
        return new Quaternion(cosYaw, 0, 0, sinYaw);
    }

    public static Quaternion fromYawAndPitch(double yaw, double pitch) {
        double yaw2 = yaw/2, pitch2 = pitch/2;
        double sinYaw = Math.sin(yaw2), cosYaw = Math.cos(yaw2), sinPitch = Math.sin(pitch2), cosPitch = Math.cos(pitch2);
        return new Quaternion(cosYaw*cosPitch, -(sinYaw*sinPitch), cosYaw*sinPitch, sinYaw*cosPitch);
    }

    public double getR() {
        return r;
    }

    public double getI() {
        return i;
    }

    public double getJ() {
        return j;
    }

    public double getK() {
        return k;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void setI(double i) {
        this.i = i;
    }

    public void setJ(double j) {
        this.j = j;
    }

    public void setK(double k) {
        this.k = k;
    }

    public void set(double r, double i, double j, double k) {
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
        double d = r*r+i*i+j*j+k*k;

        if (d == 0)
            return new Quaternion();
        else if (d != 1) {
            d = 1/Math.sqrt(d);
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
        Quaternion q = new Quaternion(0, v.getX(), v.getY(), v.getZ());
        q.multiply(this);
        return new Quaternion(r + q.getR()*0.5, i + q.getI()*0.5, j + q.getJ()*0.5, k + q.getK()*0.5);
    }

    public void add(Vector3 v) {
        set(sum(v));
    }

    public void multiply(Quaternion q) {
        set(product(q));
    }

    public Quaternion product(Quaternion q) {
        double qr = q.getR(), qi = q.getI(), qj = q.getJ(), qk = q.getK();
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

    public double toAngle() {
        return 2*Math.acos(r);
    }

    public AxisAngle toAxisAngle() {
        double x = 1, y = 1, z = 1;

        if (!inSingularity()) {
            double s = (r == 0?1:Math.sqrt(1-r*r));
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
