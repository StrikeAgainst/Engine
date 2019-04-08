package object;


import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import core.Point3;
import engine.*;

import java.util.ArrayList;

public class Maze extends SimpleObject {

    private int boardSize = 8;
    private double panelSize = 1, totalSize = boardSize*panelSize, center = panelSize+totalSize/2, wallHeight = 0.8, wallStrength = 0.1;
    private ArrayList<RigidObject> objects;

    public Maze(Point3 position, ArrayList<RigidObject> objects, InertiaTensor inertiaTensor) {
        super(new Transformation(position), Double.POSITIVE_INFINITY, inertiaTensor);
        this.objects = objects;
    }

    public void draw(GL2 gl, GLUT glut) {}

    public void drawTransformed(GL2 gl, GLUT glut) {
        for (RigidObject o : objects)
            o.render(gl, glut);
    }
}
