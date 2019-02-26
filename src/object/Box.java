package object;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import core.Vector3;
import engine.PhysicalObject;
import engine.force.InertiaTensorFactory;
import core.Point3;
import core.Quaternion;
import main.Renderer;

import java.util.Arrays;

public class Box extends PhysicalObject {

    public Vector3 size;
    private Point3[] vertexMap, vertices;

    public Box(Point3 position, Vector3 size, float mass) {
        super(position, new Quaternion(), mass, InertiaTensorFactory.forCuboid(mass, size));
        this.size = size;
    }

    public void draw(GL2 gl, GLUT glut) {
        Point3[] points = getVertices();

        gl.glColor3f(0, 0.5f, 0);
        Renderer.renderFillQuad(gl, glut, points);

        gl.glColor3f(0, 1, 0);
        Renderer.renderLineQuad(gl, glut, points);
    }

    public void drawTransformed(GL2 gl, GLUT glut) {}

    public void set(Point3 position, Quaternion orientation) {
        super.set(position, orientation);
        vertices = null;
    }

    public void updateInternals() {
        super.updateInternals();
        vertices = null;
    }

    public Vector3 getSize() {
        return size;
    }

    private void initVertexMap() {
        float halfX = size.getX() / 2, halfY = size.getY() / 2, halfZ = size.getZ() / 2;
        vertexMap = new Point3[]{
                new Point3(halfX, halfY, halfZ),
                new Point3(halfX, halfY, -halfZ),
                new Point3(halfX, -halfY, -halfZ),
                new Point3(halfX, -halfY, halfZ),
                new Point3(-halfX, -halfY, halfZ),
                new Point3(-halfX, -halfY, -halfZ),
                new Point3(-halfX, halfY, -halfZ),
                new Point3(-halfX, halfY, halfZ)
        };
    }

    public Point3[] getVertexMap() {
        if (vertexMap == null)
            initVertexMap();
        return vertexMap;
    }

    public Point3[] getVertices() {
        if (vertices == null) {
            if (vertexMap == null)
                initVertexMap();

            vertices = new Point3[8];
            for (int i = 0; i < 8; i++)
                vertices[i] = transformation.toGlobal(vertexMap[i]);
        }
        return vertices;
    }
}
