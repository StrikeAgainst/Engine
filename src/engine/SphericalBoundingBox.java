package engine;

public class SphericalBoundingBox extends BoundingBox {

	private float radius;

	public SphericalBoundingBox(EngineObject anchor, float radius) {
		super(anchor);
		this.radius = radius;
	}
	
	public boolean inside(Point3D point) {
		return (anchor.getCenter().distanceTo(point) < radius);		
	};
	
	public boolean intersect(BoundingBox bb) {
		if (bb instanceof SphericalBoundingBox) {
			return (anchor.getCenter().distanceTo(bb.getAnchor().getCenter()) < radius+((SphericalBoundingBox) bb).getRadius());
		} else {
			Point3D[] points = bb.getPoints();
			for (int i = 0; i < points.length; i++) {
				if (inside(points[i]))
					return true;
			}
			return false;
		}
	};
	
	public Point3D[] getPoints() {
		return null;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public String toString() {
		return "CBB:[anchor="+anchor.toString()+", radius="+radius+"]";
	}
}
