package object;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import engine.EngineObject;
import world.BoundingBox;
import world.Point3D;
import world.Polygon;

public class Wall extends EngineObject {

	private static final float SHADE = 0.5f;
	private final float paddingScale = 2.4f;
	private float width, height;
	private Polygon main, padding1, padding2;
	
	public Wall(Point3D center, float width, float height, boolean vertical) {
		this(center, width, height, vertical, SHADE, SHADE, SHADE);
	}
	
	public Wall(Point3D center, float width, float height, boolean vertical, float r, float g, float b) {
		super(center, new BoundingBox(center, (vertical?width/2:0.01f), (vertical?width/-2:-0.01f), (vertical?0.01f:width/2), (vertical?-0.01f:width/-2), height, 0));
		float x = center.getX(), y = center.getY(), z = center.getZ();
		main = new Polygon(new Point3D[] {
				new Point3D(x+(vertical?width/2:0), y+(vertical?0:width/2), z), 
				new Point3D(x-(vertical?width/2:0), y+(vertical?0:width/2), z+(vertical?0:height)), 
				new Point3D(x-(vertical?width/2:0), y-(vertical?0:width/2), z+height), 
				new Point3D(x+(vertical?width/2:0), y-(vertical?0:width/2), z+(vertical?height:0))});
		float pwidth = width/paddingScale, margin = (width/2)-pwidth;
		if (vertical) {
			padding1 = new Polygon(new Point3D[] {
					new Point3D(x+pwidth, y+0.01f, z+margin), 
					new Point3D(x-pwidth, y+0.01f, z+margin), 
					new Point3D(x-pwidth, y+0.01f, z+height-margin), 
					new Point3D(x+pwidth, y+0.01f, z+height-margin)});
			padding2 = new Polygon(new Point3D[] {
					new Point3D(x+pwidth, y-0.01f, z+margin), 
					new Point3D(x-pwidth, y-0.01f, z+margin), 
					new Point3D(x-pwidth, y-0.01f, z+height-margin), 
					new Point3D(x+pwidth, y-0.01f, z+height-margin)});
		} else {
			padding1 = new Polygon(new Point3D[] {
					new Point3D(x+0.01f, y+pwidth, z+margin), 
					new Point3D(x+0.01f, y-pwidth, z+margin), 
					new Point3D(x+0.01f, y-pwidth, z+height-margin), 
					new Point3D(x+0.01f, y+pwidth, z+height-margin)});
			padding2 = new Polygon(new Point3D[] {
					new Point3D(x-0.01f, y+pwidth, z+margin), 
					new Point3D(x-0.01f, y-pwidth, z+margin), 
					new Point3D(x-0.01f, y-pwidth, z+height-margin), 
					new Point3D(x-0.01f, y+pwidth, z+height-margin)});
		}
		this.width = width;
		this.setColor(r, g, b);
	}
	
	public Wall(Point3D center, boolean vertical) {
		this(center, 0.5f, 1f, vertical);
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setColor(float r, float g, float b) {
		main.setColor(r, g, b);
		padding1.setColor(r+0.1f, g+0.1f, b+0.1f);
		padding2.setColor(r+0.1f, g+0.1f, b+0.1f);
	}
	
	public void draw(GL2 gl, GLUT glut) {
		main.draw(gl, glut);
		padding1.draw(gl, glut);
		padding2.draw(gl, glut);
	}
	
	public String toString() {
		return "Wall:"+super.toString();
	}
}
