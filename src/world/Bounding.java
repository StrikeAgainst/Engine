package world;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public abstract class Bounding {

	protected Point3D anchor;
	
	public Bounding(Point3D anchor) {
		this.anchor = anchor;
	}
	
	public abstract void draw(GL2 gl, GLUT glut, boolean highlight);

	public abstract boolean encloses(Point3D point);

	public abstract boolean encloses(Bounding bounding);
	
	public Point3D getAnchor() {
		return anchor;
	}
}
