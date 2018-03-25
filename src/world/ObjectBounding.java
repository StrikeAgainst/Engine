package world;

public abstract class ObjectBounding extends Bounding {
	
	public ObjectBounding(Point3D anchor) {
		super(anchor);
	}

	public abstract Vector3D intersects(Bounding bounding);
	
	public abstract float getFrontBound(boolean relative);
	
	public abstract float getBackBound(boolean relative);
	
	public abstract float getLeftBound(boolean relative);
	
	public abstract float getRightBound(boolean relative);
	
	public abstract float getTopBound(boolean relative);
	
	public abstract float getBottomBound(boolean relative);
	
	public BoundingBox boxify() {
		return new BoundingBox(anchor, getFrontBound(true), getBackBound(true), getLeftBound(true), getRightBound(true), getTopBound(true), getBottomBound(true));
	}

	public abstract ObjectBounding clone();

	public abstract ObjectBounding clone(Point3D anchor);
}
