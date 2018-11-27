package world.bounding;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import world.Point3D;
import world.Vector3D;

public class AABBProperties extends BoundingProperties {
	
	protected float x, y, z;
	
	public AABBProperties(float x, float y, float z) {
		super();
		this.x = Math.abs(x);
		this.y = Math.abs(y);
		this.z = Math.abs(z);
	}

	public void draw(Point3D anchor, GL2 gl, GLUT glut, boolean highlight) {
		Point3D[] points = getVertices(anchor);
		if (highlight)
			gl.glColor3f(1f,0.55f,0f);
		else
			gl.glColor3f(r, g, b);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
		gl.glBegin(GL2.GL_QUADS);

		for (int quad : new int[] {0,1,2,3,2,3,4,5,4,5,6,7,6,7,0,1})
			gl.glVertex3f(points[quad].getX(), points[quad].getY(), points[quad].getZ());
		gl.glEnd();
	}
	
	public boolean encloses(Point3D anchor, Point3D point) {
		float px = point.getX(), py = point.getY(), pz = point.getZ();
		return (getBackBound(anchor) <= px && px <= getFrontBound(anchor) && getRightBound(anchor) <= py && py <= getLeftBound(anchor) && getBottomBound(anchor) <= pz && pz <= getTopBound(anchor));
	}

	public boolean encloses(Point3D anchor, Bounding bounding) {
		switch (bounding.getProperties().getClass().getSimpleName()) {
			case "AABBProperties": {
				float front = bounding.getFrontBound(), back = bounding.getBackBound(), right = bounding.getRightBound(), left = bounding.getLeftBound(), top = bounding.getTopBound(), bottom = bounding.getBottomBound();
				return (getBackBound(anchor) <= back && front <= getFrontBound(anchor) && getRightBound(anchor) <= right && left <= getLeftBound(anchor) && getBottomBound(anchor) <= bottom && top <= getTopBound(anchor));
			}
			case "BoundingSphereProperties": {
				float px = bounding.getAnchor().getX(), py = bounding.getAnchor().getY(), pz = bounding.getAnchor().getZ();
				float radius = ((BoundingSphereProperties) bounding.getProperties()).getRadius();
				return (getBackBound(anchor) <= px-radius && px+radius <= getFrontBound(anchor) && getRightBound(anchor) <= py-radius && py+radius <= getLeftBound(anchor) && getBottomBound(anchor) <= pz-radius && pz+radius <= getTopBound(anchor));
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
				boolean xBound = (getFrontBound(anchor) > bounding.getBackBound() && bounding.getFrontBound() > getBackBound(anchor));
				boolean yBound = (getLeftBound(anchor) > bounding.getRightBound() && bounding.getLeftBound() > getRightBound(anchor));
				boolean zBound = (getTopBound(anchor) > bounding.getBottomBound() && bounding.getTopBound() > getBottomBound(anchor));
				
				if (xBound && yBound && zBound)
					return new Vector3D(1f,1f,1f);
				return null;
				
			}
			case "BoundingSphereProperties": {
				Point3D anchor2 = bounding.getAnchor();
				Point3D closest = new Point3D(Math.max(getBackBound(anchor), Math.min(anchor2.getX(), getFrontBound(anchor))), Math.max(getRightBound(anchor), Math.min(anchor2.getY(), getLeftBound(anchor))), Math.max(getBottomBound(anchor), Math.min(anchor2.getZ(), getTopBound(anchor))));
				return ((closest.getEuclideanDistance(anchor2) < ((BoundingSphereProperties) bounding.getProperties()).getRadius())?closest.getVector(anchor2):null);
			}
			case "OBBProperties": {
				//todo: implement
			}
			default: {
				return null;
			}
		}
	}
	
	public Point3D[] getVertices(Point3D anchor) {
		return new Point3D[] {new Point3D(getFrontBound(anchor),getRightBound(anchor),getTopBound(anchor)),
							new Point3D(getFrontBound(anchor),getRightBound(anchor),getBottomBound(anchor)),
							new Point3D(getFrontBound(anchor),getLeftBound(anchor),getBottomBound(anchor)),
							new Point3D(getFrontBound(anchor),getLeftBound(anchor),getTopBound(anchor)),
							new Point3D(getBackBound(anchor),getLeftBound(anchor),getTopBound(anchor)),
							new Point3D(getBackBound(anchor),getLeftBound(anchor),getBottomBound(anchor)),
							new Point3D(getBackBound(anchor),getRightBound(anchor),getBottomBound(anchor)),
							new Point3D(getBackBound(anchor),getRightBound(anchor),getTopBound(anchor))};
	}

	public float getFrontBound(Point3D anchor) {
		return anchor.getX()+x;
	}

	public float getBackBound(Point3D anchor) {
		return anchor.getX()-x;
	}

	public float getLeftBound(Point3D anchor) {
		return anchor.getY()+y;
	}

	public float getRightBound(Point3D anchor) {
		return anchor.getY()-y;
	}

	public float getTopBound(Point3D anchor) {
		return anchor.getZ()+z;
	}

	public float getBottomBound(Point3D anchor) {
		return anchor.getZ()-z;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public String getType() {
		return "AABB";
	}
	
	public String toString() {
		return "AABBProperties:[x="+x+", y="+y+", z="+z+"]";
	}
}
