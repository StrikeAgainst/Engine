package world;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class BoundingSphere extends ObjectBounding {

	private float radius;

	public BoundingSphere(Point3D anchor, float radius) {
		super(anchor);
		this.radius = radius;
	}

	public boolean encloses(Point3D point) {
		return (anchor.getEuclideanDistance(point) < radius);
	}

	public boolean encloses(Bounding bounding) {
		switch (bounding.getClass().getSimpleName()) {
			case "BoundingBox": {
				for (Point3D point : ((BoundingBox) bounding).getCorners())
					if (!encloses(point))
						return false;
				return true;
			}
			case "BoundingSphere": {
				return (anchor.getEuclideanDistance(bounding.getAnchor()) + ((BoundingSphere) bounding).getRadius() < radius);
			}
			default: {
				return false;
			}
		}
	}

	public Vector3D intersects(Bounding bounding) {
		switch (bounding.getClass().getSimpleName()) {
			case "BoundingBox": {
				Point3D closest = new Point3D(Math.max(((BoundingBox) bounding).getBackBound(false), Math.min(anchor.getX(), ((BoundingBox) bounding).getFrontBound(false))), Math.max(((BoundingBox) bounding).getRightBound(false), Math.min(anchor.getY(), ((BoundingBox) bounding).getLeftBound(false))), Math.max(((BoundingBox) bounding).getBottomBound(false), Math.min(anchor.getZ(), ((BoundingBox) bounding).getTopBound(false))));
				return ((closest.getEuclideanDistance(anchor) < radius)?closest.getVector(anchor):null);
			}
			case "BoundingSphere": {
				return ((anchor.getEuclideanDistance(bounding.getAnchor()) < radius + ((BoundingSphere) bounding).getRadius())?anchor.getVector(bounding.getAnchor()):null);
			}
			default: {
				return null;
			}
		}
	}

	public float getRadius() {
		return radius;
	}
	
	public float getFrontBound(boolean relative) {
		if (relative)
			return radius;
		else
			return anchor.getX()+radius;
	}
	
	public float getBackBound(boolean relative) {
		if (relative)
			return radius;
		else
			return anchor.getX()-radius;
	}
	
	public float getLeftBound(boolean relative) {
		if (relative)
			return radius;
		else
			return anchor.getY()+radius;
	}
	
	public float getRightBound(boolean relative) {
		if (relative)
			return radius;
		else
			return anchor.getY()-radius;
	}
	
	public float getTopBound(boolean relative) {
		if (relative)
			return radius;
		else
			return anchor.getZ()+radius;
	}
	
	public float getBottomBound(boolean relative) {
		if (relative)
			return radius;
		else
			return anchor.getZ()-radius;
	}

	public void draw(GL2 gl, GLUT glut, boolean highlight) {
		gl.glTranslatef(anchor.getX(), anchor.getY(), anchor.getZ());
		if (highlight)
			gl.glColor3f(1f,0.55f,0f);
		else
			gl.glColor3f(0f,0f,1f);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
		glut.glutWireSphere(radius, 8, 8);
		gl.glTranslatef(anchor.getX() * (-1), anchor.getY() * (-1), anchor.getZ() * (-1));
	}

	public ObjectBounding clone() {
		return new BoundingSphere(new Point3D(anchor), radius);
	}

	public ObjectBounding clone(Point3D anchor) {
		return new BoundingSphere(anchor, radius);
	}

	public String toString() {
		return "BS:[anchor=" + anchor.toString() + ", radius=" + radius + "]";
	}
}
