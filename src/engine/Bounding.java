package engine;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public abstract class Bounding {

	protected Point3D anchor;
	
	public Bounding(Point3D anchor) {
		this.anchor = anchor;
	}

	public abstract boolean encloses(Point3D point);

	public abstract boolean encloses(Bounding bounding);

	public abstract boolean intersects(Bounding bounding);

	public abstract Point3D[] getPoints();
	
	public abstract void draw(GL2 gl, GLUT glut);
	
	public Point3D getAnchor() {
		return anchor;
	}
}
