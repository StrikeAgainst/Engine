package engine;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class BoundingSphere extends Bounding {

	private float radius;

	public BoundingSphere(Point3D anchor, float radius) {
		super(anchor);
		this.radius = radius;
	}

	public boolean encloses(Point3D point) {
		return (anchor.distanceTo(point) < radius);
	}

	public boolean encloses(Bounding bounding) {
		if (bounding instanceof BoundingSphere) {
			return (anchor.distanceTo(bounding.getAnchor()) + ((BoundingSphere) bounding).getRadius() < radius);
		} else {
			Point3D[] points = bounding.getPoints();
			for (int i = 0; i < points.length; i++) {
				if (!encloses(points[i]))
					return false;
			}
			return true;
		}
	}

	public boolean intersects(Bounding bounding) {
		if (bounding instanceof BoundingSphere) {
			return (anchor.distanceTo(bounding.getAnchor()) < radius + ((BoundingSphere) bounding).getRadius());
		} else {
			Point3D[] points = bounding.getPoints();
			for (int i = 0; i < points.length; i++) {
				if (encloses(points[i]))
					return true;
			}
			return false;
		}
	}

	public Point3D[] getPoints() {
		return null;
	}

	public float getRadius() {
		return radius;
	}

	public void draw(GL2 gl, GLUT glut) {
		gl.glTranslatef(anchor.getX(), anchor.getY(), anchor.getZ());
		gl.glColor3f(1f, 0.55f, 0f);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
		glut.glutWireSphere(radius, 8, 8);
		gl.glTranslatef(anchor.getX() * (-1), anchor.getY() * (-1), anchor.getZ() * (-1));
	}

	public String toString() {
		return "CBB:[anchor=" + anchor.toString() + ", radius=" + radius + "]";
	}
}
