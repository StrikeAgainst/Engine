package world;

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
	
	public void move(Vector3D vector) {
		this.x += vector.getX();
		this.y += vector.getY();
		this.z += vector.getZ();
	}
	
	public void moveTo(Point3D point) {
		this.x = point.getX();
		this.y = point.getY();
		this.z = point.getZ();
	}
	
	public Vector3D difference(Point3D point) {
		return new Vector3D(point.getX()-x,point.getY()-y,point.getZ()-z);
	}
	
	public float distanceTo(Point3D point) {
		return (float) Math.sqrt(Math.pow(point.getX()-x, 2)+Math.pow(point.getY()-y, 2)+Math.pow(point.getZ()-z, 2));
	}
	
	public Point3D clone() {
		return new Point3D(x, y, z);
	}
	
	public String toString() {
		return "Point3D:[x="+x+", y="+y+", z="+z+"]";
	}
	
	public String toStringShort() {
		return "["+x+","+y+","+z+"]";
	}
}
