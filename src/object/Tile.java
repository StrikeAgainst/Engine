package object;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import engine.EngineObject;
import world.BoundingBox;
import world.Point3D;
import world.Polygon;

public class Tile extends EngineObject {

	private static final float SHADE = 0.8f;
	private final float paddingScale = 2.1f;
	private float size;
	private Polygon main, padding;
	
	public Tile(Point3D center, float size) {
		this(center, size, SHADE, SHADE, SHADE);
	}
	
	public Tile(Point3D center, float size, float r, float g, float b) {
		super(center, new BoundingBox(center, size/2, size/-2, size/2, size/-2, 0f, -0.01f));
		float x = center.getX(), y = center.getY(), z = center.getZ();
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
		this.size = size;
		this.setColor(r, g, b);
	}
	
	public Tile(Point3D center) {
		this(center, 0.5f);
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
		return "Tile:"+super.toString();
	}
}
