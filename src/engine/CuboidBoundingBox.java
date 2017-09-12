package engine;

public class CuboidBoundingBox extends BoundingBox {
	
	private Point3D[] points;
	private float x, y, z;

	public CuboidBoundingBox(Point3D center) {
		super(center);
	}
	
	public boolean inside(Point3D point) {
		float cx = center.getX(), cy = center.getY(), cz = center.getZ();
		float px = point.getX(), py = point.getY(), pz = point.getZ();
		if (x < px && px < cx)
			if (y < py && py < cy)
				if (z < pz && pz < cz) 
					return true;
				else if (z > pz && pz > cz)
					return true;
			else if (y > py && py > cy)
				if (z < pz && pz < cz) 
					return true;
				else if (z > pz && pz > cz)
					return true;
		else if (x > px && px > cx)
			if (y < py && py < cy)
				if (z < pz && pz < cz) 
					return true;
				else if (z > pz && pz > cz)
					return true;
			else if (y > py && py > cy)
				if (z < pz && pz < cz) 
					return true;
				else if (z > pz && pz > cz)
					return true;			
		return false;
	};
	
	public boolean intersect(BoundingBox bb) {
		for (int i = 0; i < points.length; i++) {
			if (bb.inside(points[i]))
				return true;
		}
		return false;
	};
	
	public Point3D[] getPoints() {
		float cx = center.getX(), cy = center.getY(), cz = center.getZ();
		Point3D[] points = {new Point3D(cx+x,cy+y,cz+z),
							new Point3D(cx+x,cy+y,cz-z),
							new Point3D(cx+x,cy-y,cz+z),
							new Point3D(cx-x,cy+y,cz+z),
							new Point3D(cx+x,cy-y,cz-z),
							new Point3D(cx-x,cy+y,cz-z),
							new Point3D(cx-x,cy-y,cz+z),
							new Point3D(cx-x,cy-y,cz-z)};
		return points;
	}

}
