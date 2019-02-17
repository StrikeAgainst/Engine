package core;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Polygon {
	
	private Point3[] points;
	private Vector3 normal;
	private Vector3[] vectors;
	private RGB color = new RGB(1f, 1f, 1f);
	
	public Polygon(Point3[] points) throws PolygonException {
		int angles = points.length;
		if (angles < 3)
			throw new PolygonException("Three or more points are required!");

		Vector3 v1 = Vector3.offset(points[0], points[1]), v2 = Vector3.offset(points[0], points[2]);
		int i = 3;
		while (Vector3.colinear(v1, v2)) {
			if (i >= angles)
				throw new PolygonException("Polygon is a straight line!");
			v2 = Vector3.offset(points[0], points[i++]);
		}
		
		this.normal = Vector3.cross(v1, v2);
		this.vectors = new Vector3[angles];
		for (i = 0; i < angles-1; i++)
			this.vectors[i] = Vector3.offset(points[i], points[i+1]);
		this.vectors[angles-1] = Vector3.offset(points[angles-1], points[0]);
		
		if (angles > 3) {
			if (Vector3.dot(this.normal, Vector3.cross(vectors[angles-1], vectors[0])) < 0)
				throw new PolygonException("Concavity found at point 0!");
			for (i = 1; i < angles; i++) {
				if (Vector3.dot(this.normal, Vector3.cross(vectors[i-1], vectors[i])) < 0)
					throw new PolygonException("Concavity found at point "+i+"!");
			}
			for (i = 3; i < angles; i++)
				if (Vector3.dot(this.normal, Vector3.offset(points[0], points[i])) != 0)
					throw new PolygonException("Points 0 to "+i+" are not in the same layer!");
		}
		this.points = points;
	}

	public Point3[] getPoints() {
		return points;
	}
	
	public void draw(GL2 gl, GLUT glut) {
		gl.glColor3f(color.getR(),color.getG(),color.getB());
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
		gl.glBegin(GL2.GL_POLYGON);
		for (Point3 point : getPoints())
			gl.glVertex3f(point.getX(), point.getY(), point.getZ());
		gl.glEnd();
	}
	
	public void setColor(RGB color) {
		this.color = new RGB(color);
	}
	
	public boolean onPlane(Point3 p) {
		return (Vector3.dot(this.normal, Vector3.offset(points[0], p)) == 0);
	}
}