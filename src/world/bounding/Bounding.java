package world.bounding;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import world.Point3D;
import world.Vector3D;

public class Bounding {

	private Point3D anchor;
	private BoundingProperties properties;
	
	public Bounding(Point3D anchor, BoundingProperties properties) {
		this.anchor = anchor;
		this.properties = properties;
	}
	
	public void draw(GL2 gl, GLUT glut, boolean highlight) {
		properties.draw(anchor, gl, glut, highlight);
	}

	public boolean encloses(Point3D point) {
		return properties.encloses(anchor, point);
	}

	public boolean encloses(Bounding bounding) {
		return properties.encloses(anchor, bounding);
	}

	public Vector3D intersects(Bounding bounding) {
		return properties.intersects(anchor, bounding);
	}

	public float getFrontBound() {
		return properties.getFrontBound(anchor);
	}

	public float getBackBound() {
		return properties.getBackBound(anchor);
	}

	public float getLeftBound() {
		return properties.getLeftBound(anchor);
	}

	public float getRightBound() {
		return properties.getRightBound(anchor);
	}

	public float getTopBound() {
		return properties.getTopBound(anchor);
	}

	public float getBottomBound() {
		return properties.getBottomBound(anchor);
	}
	
	public Point3D getAnchor() {
		return anchor;
	}

	public BoundingProperties getProperties() {
		return properties;
	}

	public Bounding broadPhaseWith(Bounding bounding) {
		float front = Math.max(getFrontBound(), bounding.getFrontBound());
		float back = Math.max(getBackBound(), bounding.getBackBound());
		float left = Math.max(getLeftBound(), bounding.getLeftBound());
		float right = Math.max(getRightBound(), bounding.getRightBound());
		float top = Math.max(getTopBound(), bounding.getTopBound());
		float bottom = Math.max(getBottomBound(), bounding.getBottomBound());

		return new Bounding(new Point3D((front+back)/2, (left+right)/2, (top+bottom)/2), new AABBProperties((front-back)/2, (left-right)/2, (top-bottom)/2));
	}

	public Bounding clone(Point3D anchor) {
		return new Bounding(anchor, properties);
	}

	public String toString() {
		return "Bounding:[anchor="+anchor.toString()+", properties="+properties.toString()+"]";
	}
}
