package core;

public class Point3 extends Scalar3 {

	public Point3() {
		super();
	}
	
	public Point3(double x, double y, double z) {
		super(x, y, z);
	}

	public Point3(double[] p) {
		super(p);
	}

	public Point3(Scalar3 s) {
		super(s);
	}
	
	public boolean equals(Point3 p) {
		if (p == null)
			return false;
		return (this.x == p.getX() && this.y == p.getY() && this.z == p.getZ());
	}

	public Point3 offset(Vector3 v) {
		return new Point3(x+v.getX(), y+v.getY(), z+v.getZ());
	}
	
	public static Point3[] getVertexMap(Point3 upper, Point3 lower) {
		return new Point3[] {
				new Point3(upper.getX(), upper.getY(), upper.getZ()),
				new Point3(upper.getX(), upper.getY(), lower.getZ()),
				new Point3(upper.getX(), lower.getY(), lower.getZ()),
				new Point3(upper.getX(), lower.getY(), upper.getZ()),
				new Point3(lower.getX(), lower.getY(), upper.getZ()),
				new Point3(lower.getX(), lower.getY(), lower.getZ()),
				new Point3(lower.getX(), upper.getY(), lower.getZ()),
				new Point3(lower.getX(), upper.getY(), upper.getZ())
		};
	}
}
