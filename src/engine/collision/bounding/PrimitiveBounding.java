package engine.collision.bounding;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import core.Point3;
import engine.Transformation;
import engine.RigidObject;
import engine.collision.ContactDetector;
import engine.collision.PrimitiveContact;

import java.util.ArrayList;

public abstract class PrimitiveBounding extends ObjectBounding {

    protected Transformation transformationLocal;
    private Transformation transformation;

    public PrimitiveBounding(RigidObject object) {
        this(object, new Transformation());
    }

    public PrimitiveBounding(RigidObject object, Transformation transformationLocal) {
        super(object);
        this.transformationLocal = transformationLocal;
    }

    public abstract void draw(GL2 gl, GLUT glut);

    public abstract void drawTransformed(GL2 gl, GLUT glut);

    public void renderSub(GL2 gl, GLUT glut) {
        if (inContact())
            gl.glColor3f(1f, 0.55f, 0f);
        else
            gl.glColor3f(0, 0, 1f);

        this.draw(gl, glut);

        gl.glPushMatrix();
        gl.glMultMatrixf(transformationLocal.getMatrix().getDataLinear(false, true), 0);
        this.drawTransformed(gl, glut);
        gl.glPopMatrix();
    }

    public ArrayList<PrimitiveContact> contactsWith(ObjectBounding bounding) {
        ArrayList<PrimitiveContact> contacts = new ArrayList<>();
        if (bounding instanceof AssembledBounding)
            return bounding.contactsWith(this);
        else if (bounding instanceof PrimitiveBounding) {
            PrimitiveContact c = ContactDetector.BoundingOnBounding(this, (PrimitiveBounding) bounding);
            if (c != null)
                contacts.add(c);
        }

        return contacts;
    }

    public void update() {
        updateTransformation();
    }

    public void updateTransformation() {
        transformation = Transformation.combine(object.getTransformation(), transformationLocal);
    }

    public Transformation getTransformation() {
        return transformation;
    }

    public Transformation getTransformationLocal() {
        return transformationLocal;
    }

    public Point3 getPosition() {
        return getTransformation().getPosition();
    }

    public float[] getAxis(int column) {
        return getTransformation().getMatrix().getColumn(column);
    }

    public String getAttributesString() {
        return "transformationLocal="+transformationLocal.toString()+", "+super.getAttributesString();
    }
}