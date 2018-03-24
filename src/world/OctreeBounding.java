package world;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class OctreeBounding extends Bounding{
	
	protected float size;

	public OctreeBounding(Point3D root, float size) {
		super(root);
		this.size = size;
	}
	
	public Point3D[] getCorners() {
		float ax = anchor.getX(), ay = anchor.getY(), az = anchor.getZ();
		Point3D[] points = {new Point3D(ax+size,ay+size,az+size),
							new Point3D(ax+size,ay+size,az-size),
							new Point3D(ax+size,ay-size,az-size),
							new Point3D(ax+size,ay-size,az+size),
							new Point3D(ax-size,ay-size,az+size),
							new Point3D(ax-size,ay-size,az-size),
							new Point3D(ax-size,ay+size,az-size),
							new Point3D(ax-size,ay+size,az+size)};
		return points;
	}
	
	public void draw(GL2 gl, GLUT glut, boolean highlight) {
		Point3D[] points = getCorners();
		if (highlight)
			gl.glColor3f(1f,0.55f,0f);
		else
			gl.glColor3f(0f,1f,0f);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
		gl.glBegin(GL2.GL_QUADS);
		
		int[] quad_map = {0,1,2,3,2,3,4,5,4,5,6,7,6,7,0,1};
		for (int i = 0; i < quad_map.length; i++) {
			gl.glVertex3f(points[quad_map[i]].getX(), points[quad_map[i]].getY(), points[quad_map[i]].getZ());
		}
		gl.glEnd();
	}
	
	public boolean encloses(Point3D point) {
		float ax = anchor.getX(), ay = anchor.getY(), az = anchor.getZ();
		float px = point.getX(), py = point.getY(), pz = point.getZ();
		return (ax-size <= px && px <= ax+size && ay-size <= py && py <= ay+size && az-size <= pz && pz <= az+size);
	}

	public boolean encloses(Bounding bounding) {
		switch (bounding.getClass().getSimpleName()) {
			case "BoundingBox": {
				float ax = anchor.getX(), ay = anchor.getY(), az = anchor.getZ();
				float front = ((BoundingBox) bounding).getFrontBound(false), back = ((BoundingBox) bounding).getBackBound(false), right = ((BoundingBox) bounding).getRightBound(false), left = ((BoundingBox) bounding).getLeftBound(false), top = ((BoundingBox) bounding).getTopBound(false), bottom = ((BoundingBox) bounding).getBottomBound(false);
				return (ax-size <= back && front <= ax+size && ay-size <= right && left <= ay+size && az-size <= bottom && top <= az+size);
			}
			case "BoundingSphere": {
				float ax = anchor.getX(), ay = anchor.getY(), az = anchor.getZ();
				float px = bounding.getAnchor().getX(), py = bounding.getAnchor().getY(), pz = bounding.getAnchor().getZ();
				float distance = size-((BoundingSphere) bounding).getRadius();
				return (ax-distance <= px && px <= ax+distance && ay-distance <= py && py <= ay+distance && az-distance <= pz && pz <= az+distance);
			}
			default: {
				return false;
			}
		}
	}
	
	public String toString() {
		return "OB:[anchor="+anchor.toString()+", front="+size+"]";
	}
}
