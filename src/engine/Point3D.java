package engine;

public class Point3D {
	
	private float x, y, z;
	
	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float[] getArray() {
		float[] p = {x, y, z};
		return p;
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
	
	public void move(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	public float distanceTo(Point3D point) {
		float px = point.getX(), py = point.getY(), pz = point.getZ();
		return (float) Math.sqrt(Math.pow(px-x, 2)+Math.pow(py-y, 2)+Math.pow(pz-z, 2));
	}
	
	public Point3D clone() {
		return new Point3D(x, y, z);
	}
	
	public String toString() {
		return "Point3D:[x="+x+", y="+y+", z="+z+"]";
	}
}
