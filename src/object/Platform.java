package object;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import core.Point3;
import core.Quaternion;
import core.RGB;
import core.Vector3;
import engine.RigidObject;
import main.Renderer;

public class Platform extends RigidObject {

    private Vector3 size;
    private Point3[] vertexMap, vertices;

    public Platform(Point3 position, Vector3 size) {
        super(position, new Quaternion());
        this.size = size;
        this.color = new RGB(0.5,0.5,0.5);
        this.gridColor = new RGB(1,1,1);
    }

    public void draw(GL2 gl, GLUT glut) {
        Point3[] points = getVertices();
        Renderer.renderFillQuad(gl, glut, points, color);
        Renderer.renderLineQuad(gl, glut, points, gridColor);
    }

    public void drawTransformed(GL2 gl, GLUT glut) {}

    public void updateInternals() {
        super.updateInternals();
        vertices = null;
    }

    public Vector3 getSize() {
        return size;
    }

    private void initVertexMap() {
        double halfX = size.getX() / 2, halfY = size.getY() / 2, halfZ = size.getZ() / 2;
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
