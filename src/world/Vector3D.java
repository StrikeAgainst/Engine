package world;

public class Vector3D {
	
	private float x, y, z;
	
	public Vector3D(float x, float y, float z) {
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
	
	public void add(Vector3D vector) {
		this.x += vector.getX();
		this.y += vector.getY();
		this.z += vector.getZ();
	}
	
	public void reverse() {
		x = -x;
		y = -y;
		z = -z;
	}
	
	public Vector3D reverseClone() {
		return new Vector3D(-x, -y, -z);
	}
	
	public Vector3D clone() {
		return new Vector3D(x, y, z);
	}
	
	public String toString() {
		return "Vector3D:[x="+x+", y="+y+", z="+z+"]";
	}
	
	public String toStringShort() {
		return "["+x+","+y+","+z+"]";
	}

}
