package engine.collision.bounding;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import core.Point3;
import engine.Transformation;
import core.Vector3;
import engine.RigidObject;
import main.Renderer;

public class BoundingBox extends SimpleBounding {

    protected Vector3 halfSize;
    protected Point3[] vertexMap, verticesLocal, vertices;

    public BoundingBox(RigidObject object, Vector3 halfSize) {
        super(object);
        this.halfSize = halfSize;
    }

    public BoundingBox(RigidObject object, Transformation transformation, Vector3 halfSize) {
        super(object, transformation);
        this.halfSize = halfSize;
    }

    public void draw(GL2 gl, GLUT glut) {
        Renderer.renderLineQuad(gl, glut, getVerticesLocal());
    }

    public void drawTransformed(GL2 gl, GLUT glut) {}

    public Vector3 getHalfSize() {
        return halfSize;
    }

    public double transformToAxis(Vector3 axis) {
        return halfSize.getX() * Math.abs(Vector3.dot(axis, new Vector3(getAxis(0))))
             + halfSize.getY() * Math.abs(Vector3.dot(axis, new Vector3(getAxis(1))))
             + halfSize.getZ() * Math.abs(Vector3.dot(axis, new Vector3(getAxis(2))));
    }

    public void update() {
        super.update();
        vertices = null;
    }

    protected void initVertexMap() {
        if (vertexMap == null)
            vertexMap = new Point3[] {
                    new Point3(halfSize.getX(), halfSize.getY(), halfSize.getZ()),
                    new Point3(halfSize.getX(), halfSize.getY(), -halfSize.getZ()),
                    new Point3(halfSize.getX(), -halfSize.getY(), -halfSize.getZ()),
                    new Point3(halfSize.getX(), -halfSize.getY(), halfSize.getZ()),
                    new Point3(-halfSize.getX(), -halfSize.getY(), halfSize.getZ()),
                    new Point3(-halfSize.getX(), -halfSize.getY(), -halfSize.getZ()),
                    new Point3(-halfSize.getX(), halfSize.getY(), -halfSize.getZ()),
                    new Point3(-halfSize.getX(), halfSize.getY(), halfSize.getZ())
            };
    }

    public Point3[] getVertexMap() {
        if (vertexMap == null)
            initVertexMap();
        return vertexMap;
    }

    public Point3[] getVerticesLocal() {
        if (verticesLocal == null) {
            if (vertexMap == null)
                initVertexMap();

            verticesLocal = new Point3[8];
            for (int i = 0; i < 8; i++)
                verticesLocal[i] = transformationLocal.toGlobal(vertexMap[i]);
        }

        return verticesLocal;
    }

    public Point3[] getVertices() {
        if (vertices == null) {
            if (vertexMap == null)
                initVertexMap();

            vertices = new Point3[8];
            for (int i = 0; i < 8; i++)
                vertices[i] = getTransformation().toGlobal(vertexMap[i]);
        }

        return vertices;
    }

    public double getXUpperBound() {
        return getPosition().getX()+transformToAxis(new Vector3(1, 0, 0));
    }

    public double getXLowerBound() {
        return getPosition().getX()-transformToAxis(new Vector3(1, 0, 0));
    }

    public double getYUpperBound() {
        return getPosition().getY()+transformToAxis(new Vector3(0, 1, 0));
    }

    public double getYLowerBound() {
        return getPosition().getY()-transformToAxis(new Vector3(0, 1, 0));
    }

    public double getZUpperBound() {
        return getPosition().getZ()+transformToAxis(new Vector3(0, 0, 1));
    }

    public double getZLowerBound() {
        return getPosition().getZ()-transformToAxis(new Vector3(0, 0, 1));
    }

    public String getAttributesString() {
        return "halfSize="+halfSize.toString()+", "+super.getAttributesString();
    }
}
