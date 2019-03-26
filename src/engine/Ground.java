package engine;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import core.Point3;
import core.Vector3;
import engine.collision.Contact;
import engine.collision.bounding.BoundingHalfSpace;

import java.util.ArrayList;

public class Ground extends Collidable {

    protected Vector3 normal;
    protected double offset;
    protected BoundingHalfSpace bounding;
    protected Point3[] groundVertices, gridVertices;
    protected int gridSide = 5;
    protected double gridLength = 4, length;

    public Ground() {
        this(new Vector3(0, 0, 1), 0);
    }

    public Ground(Vector3 normal, double offset) {
        this.normal = normal.getNormalized();
        this.offset = offset;
        this.bounding = new BoundingHalfSpace(this.normal, this.offset);
    }

    public void render(GL2 gl, GLUT glut) {
        if (groundVertices == null || gridVertices == null) {
            length = gridSide*gridLength;
            Vector3 offset = normal.scaled(this.offset);

            Vector3[] vertexMap = new Vector3[] {new Vector3(length, length, 0), new Vector3(length, -length, 0), new Vector3(-length, -length, 0), new Vector3(-length, length, 0)};
            groundVertices = new Point3[4];

            for (int i = 0; i < 4; i++)
                groundVertices[i] = new Point3(Vector3.sum(Vector3.cross(vertexMap[i], normal), offset));

            vertexMap = new Vector3[] {new Vector3(0, 0, 0), new Vector3(0, gridLength, 0), new Vector3(gridLength, gridLength, 0), new Vector3(gridLength, 0, 0)};
            gridVertices = new Point3[4];

            for (int i = 0; i < 4; i++)
                gridVertices[i] = new Point3(Vector3.sum(Vector3.cross(vertexMap[i], normal), offset));
        }

        gl.glColor3d(0.2, 0.2, 0.2);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glBegin(GL2.GL_POLYGON);
        for (Point3 vertex : groundVertices)
            gl.glVertex3d(vertex.getX(), vertex.getY(), vertex.getZ());
        gl.glEnd();

        gl.glColor3d(0, 0, 0);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
        double gridXLength = -length, gridYLength = -length;
        for (int i = -gridSide; i < gridSide; i++) {
            for (int j = -gridSide; j < gridSide; j++) {
                gl.glBegin(GL2.GL_POLYGON);
                for (Point3 vertex : gridVertices)
                    gl.glVertex3d(vertex.getX() + gridXLength, vertex.getY() + gridYLength, vertex.getZ());
                gl.glEnd();
                gridYLength += gridLength;
            }
            gridXLength += gridLength;
            gridYLength = -length;
        }
    }

    public ArrayList<Contact> contactsWith(Collidable collidable) {
        if (collidable instanceof PhysicalObject)
            return collidable.contactsWith(this);
        return new ArrayList<>();
    }

    public Vector3 getNormal() {
        return normal;
    }

    public double getOffset() {
        return offset;
    }

    public BoundingHalfSpace getBounding() {
        return bounding;
    }
}
