package world;

public class OctreeBounding extends Bounding{
	
	protected float size;

	public OctreeBounding(Point3D root, float size) {
		super(root);
		this.size = size;
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
				return (ax-size <= back && front <= ax+size && ay-size <= left && right <= ay+size && az-size <= bottom && top <= az+size);
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
