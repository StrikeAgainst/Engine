package object;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import core.*;
import engine.RigidObject;

public class Wall extends RigidObject {

	private static final float PADDING_SCALE = 0.42f;
	private float width, height;
	private Polygon main, padding1, padding2;
	
	public Wall(Point3 position, float width, float height, boolean vertical, RGB rgb) {
		super(position, new Quaternion());

		float x = position.getX(), y = position.getY(), z = position.getZ();
        float pwidth = width*PADDING_SCALE, halfWidth = width/2, margin = halfWidth-pwidth;
        float top = z+height/2, bottom = z-height/2;
		try {
			if (vertical) {
                main = new Polygon(new Point3[] {
                        new Point3(x+halfWidth, y, bottom),
                        new Point3(x-halfWidth, y, bottom),
                        new Point3(x-halfWidth, y, top),
                        new Point3(x+halfWidth, y, top)});
                top -= margin;
                bottom += margin;
				padding1 = new Polygon(new Point3[] {
						new Point3(x+pwidth, y+0.01f, bottom),
						new Point3(x-pwidth, y+0.01f, bottom),
						new Point3(x-pwidth, y+0.01f, top),
						new Point3(x+pwidth, y+0.01f, top)});
				padding2 = new Polygon(new Point3[] {
						new Point3(x+pwidth, y-0.01f, bottom),
						new Point3(x-pwidth, y-0.01f, bottom),
						new Point3(x-pwidth, y-0.01f, top),
						new Point3(x+pwidth, y-0.01f, top)});
			} else {
                main = new Polygon(new Point3[] {
                        new Point3(x, y+halfWidth, bottom),
                        new Point3(x, y-halfWidth, bottom),
                        new Point3(x, y-halfWidth, top),
                        new Point3(x, y+halfWidth, top)});
                top -= margin;
                bottom += margin;
				padding1 = new Polygon(new Point3[] {
						new Point3(x+0.01f, y+pwidth, bottom),
						new Point3(x+0.01f, y-pwidth, bottom),
						new Point3(x+0.01f, y-pwidth, top),
						new Point3(x+0.01f, y+pwidth, top)});
				padding2 = new Polygon(new Point3[] {
						new Point3(x-0.01f, y+pwidth, bottom),
						new Point3(x-0.01f, y-pwidth, bottom),
						new Point3(x-0.01f, y-pwidth, top),
						new Point3(x-0.01f, y+pwidth, top)});
			}
		} catch (PolygonException e) {
			System.out.println("Could not create Wall: "+e.getMessage());
			System.exit(0);
		}
		this.width = width;
		this.height = height;
		this.setColor(rgb);
	}
	
	public void draw(GL2 gl, GLUT glut) {
		main.draw(gl, glut);
		padding1.draw(gl, glut);
		padding2.draw(gl, glut);
	}

	public void drawTransformed(GL2 gl, GLUT glut) {}

	public void setColor(RGB rgb) {
		main.setColor(rgb);
		rgb.scale(0.1f);
		padding1.setColor(rgb);
		padding2.setColor(rgb);
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
}
