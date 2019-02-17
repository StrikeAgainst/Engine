package object;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import core.*;
import engine.RigidObject;

public class Tile extends RigidObject {

	private static final float PADDING_SCALE = 0.48f;
	private float size;
	private Polygon main, padding;
	
	public Tile(Point3 position, float size, RGB rgb) {
		super(position, new Quaternion());

		float x = position.getX(), y = position.getY(), z = position.getZ();
		float halfSize = size/2, paddingSize = size*PADDING_SCALE;
		try {
			main = new Polygon(new Point3[] {
					new Point3(x+halfSize, y+halfSize, z),
					new Point3(x+halfSize, y-halfSize, z),
					new Point3(x-halfSize, y-halfSize, z),
					new Point3(x-halfSize, y+halfSize, z)});
			padding = new Polygon(new Point3[] {
					new Point3(x+paddingSize, y+paddingSize, z+0.01f),
					new Point3(x+paddingSize, y-paddingSize, z+0.01f),
					new Point3(x-paddingSize, y-paddingSize, z+0.01f),
					new Point3(x-paddingSize, y+paddingSize, z+0.01f)});
		} catch (PolygonException e) {
			System.out.println("Could not create Tile: "+e.getMessage());
			System.exit(0);
		}
		this.size = size;
		this.setColor(rgb);
	}
	
	public void draw(GL2 gl, GLUT glut) {
		main.draw(gl, glut);
		padding.draw(gl, glut);
	}

	public void drawTransformed(GL2 gl, GLUT glut) {}
	
	public void setColor(RGB rgb) {
		main.setColor(rgb);
		rgb.scale(0.1f);
		padding.setColor(rgb);
	}

	public float getSize() {
		return size;
	}
}
