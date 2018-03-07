package world;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Polygon {
	
	private static final double EPSILON = 1e-5;
	private Point3D[] points;
	private float r = 1.0f, g = 1.0f, b = 1.0f;
	
	public Polygon(Point3D[] points) {
		this.points = points;
	}
	
	public Point3D[] getPoints() {
		return points;
	}
	
	public void draw(GL2 gl, GLUT glut) {
		Point3D[] points = getPoints();
		gl.glColor3f(r,g,b);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
		gl.glBegin(GL2.GL_POLYGON);
		for (int i = 0; i < points.length; i++) {
			gl.glVertex3f(points[i].getX(), points[i].getY(), points[i].getZ());
		}
		gl.glEnd();
	}
	
	public void setColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public static Polygon createPolygon(Point3D[] points) {
		if (points.length < 3) {
			System.out.println("Error! Could not create Polygon! At least three points are required!");
			return null;
		}
		float[] basePoint = points[0].getArray(), dir1 = addVectors(points[1].getArray(), basePoint, 1, -1), dir2 = addVectors(points[2].getArray(), basePoint, 1, -1);
		int i = 3;
		while (colinear(dir1, dir2)) {
			if (i >= points.length) {
				System.out.println("Fehler! Das erste Polygon bildet eine Gerade!");
				return null;
			}
			dir2 = addVectors(points[i++].getArray(), basePoint, 1, -1);
		}
		for (i = 1; i < points.length; i++) {
			float[] v = addVectors(points[i].getArray(), basePoint, 1, -1);
			float[][] A = {dir1, dir2, v};
			if (!linDep(A)) {
				System.out.println("Error! Could not create Polygon! Points are not in the same layer!");
				return null;
			}
		}
		return new Polygon(points);
	}
	
	private static float[] addVectors(float[] v1, float[] v2, float factor1, float factor2) {
		int dim = v1.length;
		float[] v = new float[dim];
		for (int i = 0; i < dim; i++) {
			v[i] = v1[i]*factor1+v2[i]*factor2;
		}
		return v;
	}

	private static boolean linDep(float[][] A) {
        int N  = A.length;
        for (int p = 0; p < N; p++) {
            int max = p;
            for (int i = p + 1; i < N; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) max = i;
            }
            float[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            if (Math.abs(A[p][p]) <= EPSILON) return true;
            for (int i = p + 1; i < N; i++) {
            	float alpha = A[i][p] / A[p][p];
                for (int j = p; j < N; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }
        return false;
    }
	
	private static boolean colinear(float[] v1, float[] v2) {
		float scale;
		int dim = v1.length;
		for (int i = 0; i < dim; i++) {
			if (v2[i] != 0) { 	
				scale = v1[i]/v2[i];
				for (int j = 0; j < dim; j++) {
					if (i == j) continue;
					if (v2[j]*scale != v1[j]) return false;
				}
				return true;
			} else if (v1[i] != 0) {
				scale = v2[i]/v1[i];
				for (int j = 0; j < dim; j++) {
					if (i == j) continue;
					if (v1[j]*scale != v2[j]) return false;
				}
				return true;
			}
		}
		return true;
	}
}
