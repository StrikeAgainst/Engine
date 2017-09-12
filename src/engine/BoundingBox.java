package engine;

public abstract class BoundingBox {

	protected Point3D center;
	
	public BoundingBox(Point3D center) {
		this.center = center;
	}
	
	public abstract boolean inside(Point3D point);
	
	public abstract boolean intersect(BoundingBox bb);
	
	public Point3D getCenter() {
		return center;
	};
	
	public abstract Point3D[] getPoints();

}
