package world;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class BoundingBox extends ObjectBounding {
	
	protected float front, back, left, right, top, bottom;
	// front > back, left > right, top > bottom
	
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
		float px = point.getX(), py = point.getY(), pz = point.getZ();
		return (getBackBound(false) <= px && px <= getFrontBound(false) && getRightBound(false) <= py && py <= getLeftBound(false) && getBottomBound(false) <= pz && pz <= getTopBound(false));
	}

	public boolean encloses(Bounding bounding) {
		switch (bounding.getClass().getSimpleName()) {
			case "BoundingBox": {
				float front = ((BoundingBox) bounding).getFrontBound(false), back = ((BoundingBox) bounding).getBackBound(false), right = ((BoundingBox) bounding).getRightBound(false), left = ((BoundingBox) bounding).getLeftBound(false), top = ((BoundingBox) bounding).getTopBound(false), bottom = ((BoundingBox) bounding).getBottomBound(false);
				return (getBackBound(false) <= back && front <= getFrontBound(false) && getLeftBound(false) <= left && right <= getRightBound(false) && getBottomBound(false) <= bottom && top <= getTopBound(false));
			}
			case "BoundingSphere": {
				float px = bounding.getAnchor().getX(), py = bounding.getAnchor().getY(), pz = bounding.getAnchor().getZ();
				float radius = ((BoundingSphere) bounding).getRadius();
				return (getBackBound(false) <= px-radius && px+radius <= getFrontBound(false) && getLeftBound(false) <= py-radius && py+radius <= getRightBound(false) && getBottomBound(false) <= pz-radius && pz+radius <= getTopBound(false));
			}
			default: {
				return false;
			}
		}
	}
	
	public Vector3D intersects(Bounding bounding) {
		switch (bounding.getClass().getSimpleName()) {
			case "BoundingBox": {
				boolean xBound = (getFrontBound(false) > ((BoundingBox) bounding).getBackBound(false) && ((BoundingBox) bounding).getFrontBound(false) > getBackBound(false)); 
				boolean yBound = (getLeftBound(false) > ((BoundingBox) bounding).getRightBound(false) && ((BoundingBox) bounding).getLeftBound(false) > getRightBound(false)); 
				boolean zBound = (getTopBound(false) > ((BoundingBox) bounding).getBottomBound(false) && ((BoundingBox) bounding).getTopBound(false) > getBottomBound(false));
				
				if (xBound && yBound && zBound)
					return new Vector3D(1f,1f,1f);
				return null;
				
			}
			case "BoundingSphere": {
				Point3D anchor = bounding.getAnchor();
				Point3D closest = new Point3D(Math.max(getBackBound(false), Math.min(anchor.getX(), getFrontBound(false))), Math.max(getRightBound(false), Math.min(anchor.getY(), getLeftBound(false))), Math.max(getBottomBound(false), Math.min(anchor.getZ(), getTopBound(false))));
				return ((closest.getEuclideanDistance(anchor) < ((BoundingSphere) bounding).getRadius())?closest.getVector(anchor):null);
			}
			default: {
				return null;
			}
		}
	}
	
	public Point3D[] getCorners() {
		Point3D[] points = {new Point3D(getFrontBound(false),getRightBound(false),getTopBound(false)),
							new Point3D(getFrontBound(false),getRightBound(false),getBottomBound(false)),
							new Point3D(getFrontBound(false),getLeftBound(false),getBottomBound(false)),
							new Point3D(getFrontBound(false),getLeftBound(false),getTopBound(false)),
							new Point3D(getBackBound(false),getLeftBound(false),getTopBound(false)),
							new Point3D(getBackBound(false),getLeftBound(false),getBottomBound(false)),
							new Point3D(getBackBound(false),getRightBound(false),getBottomBound(false)),
							new Point3D(getBackBound(false),getRightBound(false),getTopBound(false))};
		return points;
	}
	
	public void draw(GL2 gl, GLUT glut, boolean highlight) {
		Point3D[] points = getCorners();
		if (highlight)
			gl.glColor3f(1f,0.55f,0f);
		else
			gl.glColor3f(0f,0f,1f);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
		gl.glBegin(GL2.GL_QUADS);
		
		int[] quad_map = {0,1,2,3,2,3,4,5,4,5,6,7,6,7,0,1};
		for (int i = 0; i < quad_map.length; i++) {
			gl.glVertex3f(points[quad_map[i]].getX(), points[quad_map[i]].getY(), points[quad_map[i]].getZ());
		}
		gl.glEnd();
	}
	
	public float getFrontBound(boolean relative) {
		if (relative)
			return front;
		else
			return anchor.getX()+front;
	}
	
	public float getBackBound(boolean relative) {
		if (relative)
			return back;
		else
			return anchor.getX()-back;
	}
	
	public float getLeftBound(boolean relative) {
		if (relative)
			return left;
		else
			return anchor.getY()+left;
	}
	
	public float getRightBound(boolean relative) {
		if (relative)
			return right;
		else
			return anchor.getY()-right;
	}
	
	public float getTopBound(boolean relative) {
		if (relative)
			return top;
		else
			return anchor.getZ()+top;
	}
	
	public float getBottomBound(boolean relative) {
		if (relative)
			return bottom;
		else
			return anchor.getZ()-bottom;
	}
	
	public BoundingBox boxify() {
		return (BoundingBox) clone();
	};
	
	public ObjectBounding clone() {
		return new BoundingBox(anchor.clone(), front, back, left, right, top, bottom);
	}
	
	public ObjectBounding clone(Point3D anchor) {
		return new BoundingBox(anchor, front, back, left, right, top, bottom);
	}
	
	public String toString() {
		return "BB:[anchor="+anchor.toString()+", front="+getFrontBound(false)+", back="+getBackBound(false)+", left="+getLeftBound(false)+", right="+getRightBound(false)+", top="+getTopBound(false)+", bottom="+getBottomBound(false)+"]";
	}
}
