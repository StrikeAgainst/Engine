package world.bounding;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import world.Point3D;
import world.Vector3D;

public class BoundingSphereProperties extends BoundingProperties {

	private float radius;

	public BoundingSphereProperties(float radius) {
		super();
		this.radius = radius;
	}

	public void draw(Point3D anchor, GL2 gl, GLUT glut, boolean highlight) {
		gl.glTranslatef(anchor.getX(), anchor.getY(), anchor.getZ());
		if (highlight)
			gl.glColor3f(1f,0.55f,0f);
		else
			gl.glColor3f(r, g, b);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
		glut.glutWireSphere(radius, 8, 8);
		gl.glTranslatef(anchor.getX() * (-1), anchor.getY() * (-1), anchor.getZ() * (-1));
	}

	public boolean encloses(Point3D anchor, Point3D point) {
		return (anchor.getEuclideanDistance(point) < radius);
	}

	public boolean encloses(Point3D anchor, Bounding bounding) {
		switch (bounding.getProperties().getClass().getSimpleName()) {
			case "AABBProperties": {
				for (Point3D point : ((AABBProperties) bounding.getProperties()).getVertices(bounding.getAnchor()))
					if (!encloses(anchor, point))
						return false;
				return true;
			}
			case "BoundingSphereProperties": {
				return (anchor.getEuclideanDistance(bounding.getAnchor()) + ((BoundingSphereProperties) bounding.getProperties()).getRadius() < radius);
			}
			case "OBBProperties": {
				for (Point3D point : ((OBBProperties) bounding.getProperties()).getVertices(bounding.getAnchor()))
					if (!encloses(anchor, point))
						return false;
				return true;
			}
			default: {
				return false;
			}
		}
	}

	public Vector3D intersects(Point3D anchor, Bounding bounding) {
		switch (bounding.getProperties().getClass().getSimpleName()) {
			case "AABBProperties": {
				Point3D closest = new Point3D(Math.max(bounding.getBackBound(), Math.min(anchor.getX(), bounding.getFrontBound())), Math.max(bounding.getRightBound(), Math.min(anchor.getY(), bounding.getLeftBound())), Math.max(bounding.getBottomBound(), Math.min(anchor.getZ(), bounding.getTopBound())));
				return ((closest.getEuclideanDistance(anchor) < radius)?closest.getVector(anchor):null);
			}
			case "BoundingSphereProperties": {
				return ((anchor.getEuclideanDistance(bounding.getAnchor()) < radius + ((BoundingSphereProperties) bounding.getProperties()).getRadius())?anchor.getVector(bounding.getAnchor()):null);
			}
			case "OBBProperties": {
				//todo: implement
			}
			default: {
				return null;
			}
		}
	}

	public float getRadius() {
		return radius;
	}
	
	public float getFrontBound(Point3D anchor) {
		return anchor.getX()+radius;
	}
	
	public float getBackBound(Point3D anchor) {
		return anchor.getX()-radius;
	}
	
	public float getLeftBound(Point3D anchor) {
		return anchor.getY()+radius;
	}
	
	public float getRightBound(Point3D anchor) {
		return anchor.getY()-radius;
	}
	
	public float getTopBound(Point3D anchor) {
		return anchor.getZ()+radius;
	}
	
	public float getBottomBound(Point3D anchor) {
		return anchor.getZ()-radius;
	}

	public String toString() {
		return "BS:[radius=" + radius + "]";
	}
}
