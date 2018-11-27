package world;

public class Vector3D {
	
	private float x, y, z;

	public Vector3D() {
		this(0, 0, 0);
	}
	
	public Vector3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

    public Vector3D(Point3D p1, Point3D p2) {
        this.x = p2.getX() - p1.getX();
        this.y = p2.getY() - p1.getY();
        this.z = p2.getZ() - p1.getZ();
    }

	public Vector3D(Vector3D v) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
	}

	public Vector3D(Vector3D v, float s) {
		this.x = v.getX()*s;
		this.y = v.getY()*s;
		this.z = v.getZ()*s;
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
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	public void add(Vector3D v) {
		this.x += v.getX();
		this.y += v.getY();
		this.z += v.getZ();
	}

	public void subtract(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

	public void subtract(Vector3D v) {
		this.x -= v.getX();
		this.y -= v.getY();
		this.z -= v.getZ();
	}
	
	public static Vector3D sum(Vector3D v1, Vector3D v2) {
		return new Vector3D(v1.getX()+v2.getX(), v1.getY()+v2.getY(), v1.getZ()+v2.getZ());
	}

	public static Vector3D difference(Vector3D v1, Vector3D v2) {
		return new Vector3D(v1.getX()-v2.getX(), v1.getY()-v2.getY(), v1.getZ()-v2.getZ());
	}
	
	public void stretch(float s) {
		this.x *= s;
		this.y *= s;
		this.z *= s;
	}

	public void shrink(float s) {
		stretch(1/s);
	}
	
	public static Vector3D product(Vector3D v, float s) {
		return new Vector3D(v.getX()*s, v.getY()*s, v.getZ()*s);
	}

	public static Vector3D product(Vector3D v, Matrix3x3 m) {
		float x = v.getX(), y = v.getY(), z = v.getZ();
		float[][] mData = m.getData();
		return new Vector3D(
				x*mData[0][0] + y*mData[1][0] + z*mData[2][0],
				x*mData[0][1] + y*mData[1][1] + z*mData[2][1],
				x*mData[0][2] + y*mData[1][2] + z*mData[2][2]);
	}

	public static Vector3D product(Vector3D v, Matrix3x4 m) {
		float x = v.getX(), y = v.getY(), z = v.getZ();
		float[][] mData = m.getData();
		return new Vector3D(
				x*mData[0][0] + y*mData[1][0] + z*mData[2][0] + mData[3][0],
				x*mData[0][1] + y*mData[1][1] + z*mData[2][1] + mData[3][1],
				x*mData[0][2] + y*mData[1][2] + z*mData[2][2] + mData[3][2]);
	}
	
	public static float dot(Vector3D v1, Vector3D v2) {
		return v1.getX()*v2.getX()+v1.getY()*v2.getY()+v1.getZ()*v2.getZ();
	}
	
	public static Vector3D cross(Vector3D v1, Vector3D v2) {
		float x1 = v1.getX(), y1 = v1.getY(), z1 = v1.getZ();
		float x2 = v2.getX(), y2 = v2.getY(), z2 = v2.getZ();
		return new Vector3D(y1*z2-y2*z1, z1*x2-z2*x1, x1*y2-x2*y1);
	}
	
	public float getBlockLength() {
		return Math.abs(x)+Math.abs(y)+Math.abs(z);
	}
	
	public float getEuclideanLength() {
		return (float) Math.sqrt(x*x+y*y+z*z);
	}

    public Vector3D normalize() {
        float scale = 1/getEuclideanLength();
        return new Vector3D(getX()*scale, getY()*scale, getZ()*scale);
    }

	public boolean isNull() {
		return (this.x == 0 && this.y == 0 && this.z == 0);
	}

    public void nullify() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public static boolean colinear(Vector3D v1, Vector3D v2) {
		return (colinear_scale(v1, v2) != 0);
	}
	
	public static float colinear_scale(Vector3D v1, Vector3D v2) {
		float scale;
		if (v1.getX() != 0 && v2.getX() != 0)
			scale = v1.getX()/v2.getX();
		else if (v1.getY() != 0 && v2.getY() != 0)
			scale = v1.getY()/v2.getY();
		else if (v1.getZ() != 0 && v2.getZ() != 0)
			scale = v1.getZ()/v2.getZ();
		else
			return 0;
		return (v1.equals(Vector3D.product(v2, scale))?scale:0);
	}
	
	public void reverse() {
		x = -x;
		y = -y;
		z = -z;
	}
	
	public static Vector3D reverse(Vector3D v) {
		return new Vector3D(-v.getX(), -v.getY(), -v.getZ());
	}
	
	public float[] getArray() {
		return new float[] {x, y, z};
	}
	
	public boolean equals(Vector3D v) {
		return (this.x == v.getX() && this.y == v.getY() && this.z == v.getZ());
	}
	
	public String toString() {
		return "Vector3D:[x="+x+", y="+y+", z="+z+"]";
	}
	
	public String toStringShort() {
		return "["+x+","+y+","+z+"]";
	}

}
