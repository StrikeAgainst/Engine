package world;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Polygon {
	
	private Point3D[] points;
	private Vector3D normal;
	private Vector3D[] vectors;
	private float r = 1.0f, g = 1.0f, b = 1.0f;
	
	public Polygon(Point3D[] points) throws PolygonException {
		int angles = points.length;
		if (angles < 3)
			throw new PolygonException("Three or more points are required!");

		Vector3D v1 = points[0].getVector(points[1]), v2 = points[0].getVector(points[2]);
		int i = 3;
		while (Vector3D.colinear(v1, v2)) {
			if (i >= angles)
				throw new PolygonException("Polygon is a straight line!");
			v2 = points[0].getVector(points[i++]);
		}
		
		this.normal = Vector3D.cross(v1, v2);
		this.vectors = new Vector3D[angles];
		for (i = 0; i < angles-1; i++)
			this.vectors[i] = points[i].getVector(points[i+1]);
		this.vectors[angles-1] = points[angles-1].getVector(points[0]);
		
		if (angles > 3) {
			if (Vector3D.dot(this.normal, Vector3D.cross(vectors[angles-1], vectors[0])) < 0)
				throw new PolygonException("Concavity found at point 0!");
			for (i = 1; i < angles; i++) {
				if (Vector3D.dot(this.normal, Vector3D.cross(vectors[i-1], vectors[i])) < 0)
					throw new PolygonException("Concavity found at point "+i+"!");
			}
			for (i = 3; i < angles; i++)
				if (Vector3D.dot(this.normal, points[0].getVector(points[i])) != 0)
					throw new PolygonException("Points 0 to "+i+" are not in the same layer!");
		}
		this.points = points;
	}

	public Point3D[] getPoints() {
		return points;
	}
	
	public void draw(GL2 gl, GLUT glut) {
		gl.glColor3f(r,g,b);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
		gl.glBegin(GL2.GL_POLYGON);
		for (Point3D point : getPoints())
			gl.glVertex3f(point.getX(), point.getY(), point.getZ());
		gl.glEnd();
	}
	
	public void setColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public boolean onPlane(Point3D p) {
		return (Vector3D.dot(this.normal, points[0].getVector(p)) == 0);
	}
}