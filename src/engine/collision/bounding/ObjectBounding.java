package engine.collision.bounding;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import engine.RigidObject;
import engine.Transformation;
import engine.collision.BroadPhaseContact;
import engine.collision.PrimitiveContact;

import java.util.ArrayList;

public abstract class ObjectBounding extends CollidableBounding {

    public static boolean VISIBLE = false;

    protected RigidObject object;
    protected BroadPhase broadphase;

    public ObjectBounding(RigidObject object) {
        this.object = object;
        broadphase = new BroadPhase(this);
    }

    public void render(GL2 gl, GLUT glut) {
        if (VISIBLE)
            renderSub(gl, glut);
    }

    protected abstract void renderSub(GL2 gl, GLUT glut);

    public RigidObject getObject() {
        return object;
    }

    public void updateAll() {
        update();
        broadphase.update();
    }

    public abstract ArrayList<PrimitiveContact> contactsWith(ObjectBounding bounding);

    public BroadPhaseContact broadphaseContactWith(ObjectBounding bounding) {
        return broadphase.contactWith(bounding.getBroadPhase());
    }

    public abstract void update();

    public BroadPhase getBroadPhase() {
        return broadphase;
    }

    public Transformation getTransformation() {
        return object.getTransformation();
    }

    public String getNameString() {
        return object.getNameIDString()+":"+super.getNameString();
    }

    public String getAttributesString() {
        return "broadphase="+getBroadPhase().toString();
    }

}
