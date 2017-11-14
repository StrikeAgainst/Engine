package engine;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Tile extends EngineObject {

	private final float paddingScale = 2.1f;
	private float size, shade = 0.8f;
	private Polygon main, padding;
	
	public Tile(Point3D center, float size) {
		super(center);
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
		this.setColor(shade, shade, shade);
		applyBounding(new BoundingBox(center, size/2, size/-2, size/2, size/-2, 0.01f, -0.01f));
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
