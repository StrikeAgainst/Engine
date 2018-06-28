package world;

public class Point3D {
	
	private float x, y, z;
	public final static Point3D ROOT = new Point3D(0f,0f,0f);
	
	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3D(Point3D p) {
		this.x = p.getX();
		this.y = p.getY();
		this.z = p.getZ();
	}

	public Point3D(Point3D p, float x, float y, float z) {
		this.x = p.getX()+x;
		this.y = p.getY()+y;
		this.z = p.getZ()+z;
	}

	public Point3D(Point3D p, Vector3D v) {
		this.x = p.getX()+v.getX();
		this.y = p.getY()+v.getY();
		this.z = p.getZ()+v.getZ();
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
	
	public void move(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	public void move(Vector3D v) {
		this.x += v.getX();
		this.y += v.getY();
		this.z += v.getZ();
	}
	
	public void moveTo(Point3D p) {
		this.x = p.getX();
		this.y = p.getY();
		this.z = p.getZ();
	}
	
	public Vector3D getVector(Point3D p) {
		return new Vector3D(p.getX()-x,p.getY()-y,p.getZ()-z);
	}
	
	public float getBlockDistance(Point3D p) {
		return Math.abs(p.getX()-x)+Math.abs(p.getY()-y)+Math.abs(p.getZ()-z);
	}
	
	public float getEuclideanDistance(Point3D p) {
		return (float) Math.sqrt(Math.pow(p.getX()-x, 2)+Math.pow(p.getY()-y, 2)+Math.pow(p.getZ()-z, 2));
	}
	
	public float[] getArray() {
		return new float[] {x, y, z};
	}
	
	public boolean equals(Point3D p) {
		return (this.x == p.getX() && this.y == p.getY() && this.z == p.getZ());
	}
	
	public String toString() {
		return "Point3D:[x="+x+", y="+y+", z="+z+"]";
	}
	
	public String toStringShort() {
		return "["+x+","+y+","+z+"]";
	}
}
