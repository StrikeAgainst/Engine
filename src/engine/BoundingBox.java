package engine;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class BoundingBox extends Bounding {
	
	protected float front, back, left, right, top, bottom;

	public BoundingBox(Point3D anchor, float front, float back, float left, float right, float top, float bottom) {
		super(anchor);
		this.front = Math.abs(front);
		this.back = Math.abs(back);
		this.left = Math.abs(left);
		this.right = Math.abs(right);
		this.top = Math.abs(top);
		this.bottom = Math.abs(bottom);
	}
	
	public boolean encloses(Point3D point) {
		float ax = anchor.getX(), ay = anchor.getY(), az = anchor.getZ();
		float px = point.getX(), py = point.getY(), pz = point.getZ();
		return (ax-back < px && px < ax+front && ay-left < py && py < ay+right && az-bottom < pz && pz < az+top);
	}

	public boolean encloses(Bounding bounding) {
		if (bounding instanceof BoundingSphere) {
			float ax = anchor.getX(), ay = anchor.getY(), az = anchor.getZ();
			float px = bounding.getAnchor().getX(), py = bounding.getAnchor().getY(), pz = bounding.getAnchor().getZ();
			float radius = ((BoundingSphere) bounding).getRadius();
			return (ax-back < px-radius && px+radius < ax+front && ay-left < py-radius && py+radius < ay+right && az-bottom < pz-radius && pz+radius < az+top);
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
		Point3D[] points = getPoints();
		for (int i = 0; i < points.length; i++) {
			if (bounding.encloses(points[i]))
				return true;
		}
		return false;
	}
	
	public Point3D[] getPoints() {
		float ax = anchor.getX(), ay = anchor.getY(), az = anchor.getZ();
		Point3D[] points = {new Point3D(ax+front,ay+right,az+top),
							new Point3D(ax+back,ay+right,az-bottom),
							new Point3D(ax+back,ay-left,az-bottom),
							new Point3D(ax+front,ay-left,az+top),
							new Point3D(ax-back,ay-left,az+top),
							new Point3D(ax-front,ay-left,az-bottom),
							new Point3D(ax-front,ay+right,az-bottom),
							new Point3D(ax-back,ay+right,az+top)};
		return points;
	}
	
	public void draw(GL2 gl, GLUT glut) {
		Point3D[] points = getPoints();
		gl.glColor3f(1f,0.55f,0f);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
		gl.glBegin(GL2.GL_QUADS);
		
		int[] quad_map = {0,1,2,3,2,3,4,5,4,5,6,7,6,7,0,1};
		for (int i = 0; i < quad_map.length; i++) {
			gl.glVertex3f(points[quad_map[i]].getX(), points[quad_map[i]].getY(), points[quad_map[i]].getZ());
		}
		gl.glEnd();
	}
	
	public float getFrontBound() {
		return front;
	}
	
	public float getBackBound() {
		return back;
	}
	
	public float getLeftBound() {
		return left;
	}
	
	public float getRightBound() {
		return right;
	}
	
	public float getTopBound() {
		return top;
	}
	
	public float getBottomBound() {
		return bottom;
	}
	
	public String toString() {
		return "CBB:[anchor="+anchor.toString()+", front="+front+", back="+back+", left="+left+", right="+right+", top="+top+", bottom="+bottom+"]";
	}
}
