package world;

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

    public void normalize() {
        float d = r*r+i*i+j*j+k*k;

        if (d == 0)
            r = 1;
        else {
            d = (float) (1/Math.sqrt(d));
            r *= d;
            i *= d;
            j *= d;
            k *= d;
        }
    }

    public Quaternion rotate(Vector3D v) {
        return product(new Quaternion(0, v.getX(), v.getY(), v.getZ()));
    }

    public void add(Vector3D v) {
        Quaternion q = rotate(v);
        r += q.getR()*0.5f;
        i += q.getI()*0.5f;
        j += q.getJ()*0.5f;
        k += q.getK()*0.5f;
    }

    public Quaternion product(Quaternion q) {
        float qr = q.getR(), qi = q.getI(), qj = q.getJ(), qk = q.getK();
        return new Quaternion(
                r*qr - i*qi - j*qj - k*qk,
                r*qi + i*qr + j*qk - k*qj,
                r*qj + j*qr + k*qi - i*qk,
                r*qk + k*qr + i*qj - j*qi);
    }
}
