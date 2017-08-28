package engine;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Tile extends EngineObject {

	private final float paddingScale = 2.1f;
	private float x, y, z, size, shade = 0.8f;
	private Polygon main, padding;
	
	public Tile(float x, float y, float z, float size) {
		super(x, y, z, size/2, size/-2, size/2, size/-2, 0.01f, -0.01f);
		main = new Polygon(new Point3D[] {
				new Point3D(x+size/2, y+size/2, z), 
				new Point3D(x+size/2, y-size/2, z), 
				new Point3D(x-size/2, y-size/2, z), 
				new Point3D(x-size/2, y+size/2, z)});
		padding = new Polygon(new Point3D[] {
				new Point3D(x+size/paddingScale, y+size/paddingScale, z+0.01f), 
				new Point3D(x+size/paddingScale, y-size/paddingScale, z+0.01f), 
				new Point3D(x-size/paddingScale, y-size/paddingScale, z+0.01f), 
				new Point3D(x-size/paddingScale, y+size/paddingScale, z+0.01f)});
		this.x = x;
		this.y = y;
		this.z = z;
		this.size = size;
		this.setColor(shade, shade, shade);
	}
	
	public Tile(float x, float y, float z) {
		this(x, y, z, 0.5f);
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
	
	public float getSize() {
		return size;
	}
	
	public void draw(GL2 gl, GLUT glut) {
		main.draw(gl, glut);
		padding.draw(gl, glut);
	}
	
	public void setColor(float r, float g, float b) {
		main.setColor(r, g, b);
		padding.setColor(r+0.1f, g+0.1f, b+0.1f);
	}
	
	public String toString() {
		return "Tile[x="+x+", y="+y+",z="+z+"]";
	}
}
