package world;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public abstract class ObjectBounding extends Bounding {
	
	public ObjectBounding(Point3D anchor) {
		super(anchor);
	}

	public abstract Vector3D intersects(Bounding bounding);
	
	public abstract void draw(GL2 gl, GLUT glut, boolean highlight);
}
