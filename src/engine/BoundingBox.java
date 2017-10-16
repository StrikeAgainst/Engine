package engine;

public abstract class BoundingBox {

	protected EngineObject anchor;
	
	public BoundingBox(EngineObject anchor) {
		this.anchor = anchor;
	}
	
	public abstract boolean inside(Point3D point);
	
	public abstract boolean intersect(BoundingBox bb);
	
	public abstract Point3D[] getPoints();

}
