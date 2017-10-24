package engine;

public class CuboidBoundingBox extends BoundingBox {
	
	protected float front, back, left, right, top, bottom;

	public CuboidBoundingBox(EngineObject anchor, float front, float back, float left, float right, float top, float bottom) {
		super(anchor);
		this.front = Math.abs(front);
		this.back = Math.abs(back);
		this.left = Math.abs(left);
		this.right = Math.abs(right);
		this.top = Math.abs(top);
		this.bottom = Math.abs(bottom);
	}
	
	public boolean inside(Point3D point) {
		float ax = anchor.getX(), ay = anchor.getY(), az = anchor.getZ();
		float px = point.getX(), py = point.getY(), pz = point.getZ();
		return (ax-back < px && px < ax+front && ay-left < py && py < ay+right && az-bottom < pz && pz < az+top);
	};
	
	public boolean intersect(BoundingBox bb) {
		Point3D[] points = getPoints();
		for (int i = 0; i < points.length; i++) {
			if (bb.inside(points[i]))
				return true;
		}
		return false;
	};
	
	public Point3D[] getPoints() {
		float ax = anchor.getX(), ay = anchor.getY(), az = anchor.getZ();
		Point3D[] points = {new Point3D(ax+front,ay+right,az+top),
							new Point3D(ax+front,ay-left,az+top),
							new Point3D(ax-back,ay-left,az+top),
							new Point3D(ax-back,ay+right,az+top),
							new Point3D(ax+front,ay+right,az-bottom),
							new Point3D(ax+front,ay-left,az-bottom),
							new Point3D(ax-back,ay-left,az-bottom),
							new Point3D(ax-back,ay+right,az-bottom)};
		return points;
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

}
