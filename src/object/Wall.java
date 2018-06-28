package object;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import engine.EngineObject;
import world.bounding.AABBProperties;
import world.Point3D;
import world.Polygon;
import world.PolygonException;

public class Wall extends EngineObject {

	private static final float SHADE = 0.5f, PADDING_SCALE = 2.4f;
	private float width, height;
	private Polygon main, padding1, padding2;
	
	public Wall(Point3D anchor, float width, float height, boolean vertical) {
		this(anchor, width, height, vertical, SHADE, SHADE, SHADE);
	}
	
	public Wall(Point3D anchor, float width, float height, boolean vertical, float r, float g, float b) {
		super(anchor, new AABBProperties((vertical?width/2:0.01f), (vertical?0.01f:width/2), height/2));
		float x = anchor.getX(), y = anchor.getY(), z = anchor.getZ();
        float pwidth = width/PADDING_SCALE, margin = (width/2)-pwidth;
        float top = z+height/2, bottom = z-height/2;
		try {
			if (vertical) {
                main = new Polygon(new Point3D[] {
                        new Point3D(x+width/2, y, bottom),
                        new Point3D(x-width/2, y, bottom),
                        new Point3D(x-width/2, y, top),
                        new Point3D(x+width/2, y, top)});
                top -= margin;
                bottom += margin;
				padding1 = new Polygon(new Point3D[] {
						new Point3D(x+pwidth, y+0.01f, bottom),
						new Point3D(x-pwidth, y+0.01f, bottom),
						new Point3D(x-pwidth, y+0.01f, top),
						new Point3D(x+pwidth, y+0.01f, top)});
				padding2 = new Polygon(new Point3D[] {
						new Point3D(x+pwidth, y-0.01f, bottom),
						new Point3D(x-pwidth, y-0.01f, bottom),
						new Point3D(x-pwidth, y-0.01f, top),
						new Point3D(x+pwidth, y-0.01f, top)});
			} else {
                main = new Polygon(new Point3D[] {
                        new Point3D(x, y+width/2, bottom),
                        new Point3D(x, y-width/2, bottom),
                        new Point3D(x, y-width/2, top),
                        new Point3D(x, y+width/2, top)});
                top -= margin;
                bottom += margin;
				padding1 = new Polygon(new Point3D[] {
						new Point3D(x+0.01f, y+pwidth, bottom),
						new Point3D(x+0.01f, y-pwidth, bottom),
						new Point3D(x+0.01f, y-pwidth, top),
						new Point3D(x+0.01f, y+pwidth, top)});
				padding2 = new Polygon(new Point3D[] {
						new Point3D(x-0.01f, y+pwidth, bottom),
						new Point3D(x-0.01f, y-pwidth, bottom),
						new Point3D(x-0.01f, y-pwidth, top),
						new Point3D(x-0.01f, y+pwidth, top)});
			}
		} catch (PolygonException e) {
			System.out.println("Could not create Wall: "+e.getMessage());
			System.exit(0);
		}
		this.width = width;
		this.height = height;
		this.setColor(r, g, b);
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
}
