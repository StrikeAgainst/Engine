package world;

public abstract class Bounding {

	protected Point3D anchor;
	
	public Bounding(Point3D anchor) {
		this.anchor = anchor;
	}

	public abstract boolean encloses(Point3D point);

	public abstract boolean encloses(Bounding bounding);
	
	public Point3D getAnchor() {
		return anchor;
	}
}
