package object;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import engine.EngineObject;
import world.bounding.AABBProperties;
import world.Point3D;
import world.Polygon;
import world.PolygonException;

public class Tile extends EngineObject {

	private static final float SHADE = 0.8f, PADDING_SCALE = 2.1f;
	private float size;
	private Polygon main, padding;
	
	public Tile(Point3D anchor, float size) {
		this(anchor, size, SHADE, SHADE, SHADE);
	}
	
	public Tile(Point3D anchor, float size, float r, float g, float b) {
		super(anchor, new AABBProperties(size/2, size/2, 0.01f));
		float x = anchor.getX(), y = anchor.getY(), z = anchor.getZ();
		try {
			main = new Polygon(new Point3D[] {
					new Point3D(x+size/2, y+size/2, z), 
					new Point3D(x+size/2, y-size/2, z), 
					new Point3D(x-size/2, y-size/2, z), 
					new Point3D(x-size/2, y+size/2, z)});
			padding = new Polygon(new Point3D[] {
					new Point3D(x+size/PADDING_SCALE, y+size/PADDING_SCALE, z+0.01f),
					new Point3D(x+size/PADDING_SCALE, y-size/PADDING_SCALE, z+0.01f),
					new Point3D(x-size/PADDING_SCALE, y-size/PADDING_SCALE, z+0.01f),
					new Point3D(x-size/PADDING_SCALE, y+size/PADDING_SCALE, z+0.01f)});
		} catch (PolygonException e) {
			System.out.println("Could not create Tile: "+e.getMessage());
			System.exit(0);
		}
		this.size = size;
		this.setColor(r, g, b);
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
}
