package core;

public class Vector3 extends Scalar3{

	public Vector3() {
		super();
	}
	
	public Vector3(double x, double y, double z) {
		super(x, y, z);
	}

	public Vector3(double[] v) {
		super(v);
	}

	public Vector3(Scalar3 s) {
		super(s);
	}

	public static Vector3 offset(Point3 p1, Point3 p2) {
		return new Vector3(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ());
	}

	public static Vector3 sum(Vector3 v1, Vector3 v2) {
		return new Vector3(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
	}

	public static double dot(Vector3 v1, Vector3 v2) {
		return v1.getX()*v2.getX()+v1.getY()*v2.getY()+v1.getZ()*v2.getZ();
	}

	public static Vector3 cross(Vector3 v1, Vector3 v2) {
		double x1 = v1.getX(), y1 = v1.getY(), z1 = v1.getZ();
		double x2 = v2.getX(), y2 = v2.getY(), z2 = v2.getZ();
		return new Vector3(y1*z2-y2*z1, z1*x2-z2*x1, x1*y2-x2*y1);
	}

	public static boolean colinear(Vector3 v1, Vector3 v2) {
		return (colinear_scale(v1, v2) != 0);
	}

	public static double colinear_scale(Vector3 v1, Vector3 v2) {
		double scale;
		if (v1.getX() != 0 && v2.getX() != 0)
			scale = v1.getX()/v2.getX();
		else if (v1.getY() != 0 && v2.getY() != 0)
			scale = v1.getY()/v2.getY();
		else if (v1.getZ() != 0 && v2.getZ() != 0)
			scale = v1.getZ()/v2.getZ();
		else
			return 0;
		return (v1.equals(v2.scaled(scale))?scale:0);
	}

	public Vector3 scaled(double s) {
		return new Vector3(x*s, y*s, z*s);
	}

	public Vector3 product(Matrix3x3 m) {
		double[][] mData = m.getData();
		return new Vector3(
				x*mData[0][0] + y*mData[1][0] + z*mData[2][0],
				x*mData[0][1] + y*mData[1][1] + z*mData[2][1],
				x*mData[0][2] + y*mData[1][2] + z*mData[2][2]);
	}

	public Vector3 product(Matrix3x4 m) {
		double[][] mData = m.getData();
		return new Vector3(
				x*mData[0][0] + y*mData[1][0] + z*mData[2][0] + mData[3][0],
				x*mData[0][1] + y*mData[1][1] + z*mData[2][1] + mData[3][1],
				x*mData[0][2] + y*mData[1][2] + z*mData[2][2] + mData[3][2]);
	}
	
	public double getBlockMagnitude() {
		return Math.abs(x)+Math.abs(y)+Math.abs(z);
	}

	public double getSquareMagnitude() {
		return x*x+y*y+z*z;
	}
	
	public double getEuclideanMagnitude() {
		return Math.sqrt(getSquareMagnitude());
	}

	public void normalize() {
		set(getNormalized());
	}

    public Vector3 getNormalized() {
        double scale = 1/ getEuclideanMagnitude();
        return new Vector3(getX()*scale, getY()*scale, getZ()*scale);
    }

	public Vector3 projection(Vector3 on) {
		return on.scaled(Vector3.dot(on, this)/Vector3.dot(on, on));
	}

	public boolean isNull() {
		return (this.x == 0 && this.y == 0 && this.z == 0);
	}
	
	public void revert() {
		set(getReverse());
	}
	
	public Vector3 getReverse() {
		return new Vector3(-x, -y, -z);
	}
	
	public boolean equals(Vector3 v) {
		if (v == null)
			return false;
		return (this.x == v.getX() && this.y == v.getY() && this.z == v.getZ());
	}
}
