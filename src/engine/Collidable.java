package engine;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import engine.collision.Contact;
import engine.collision.bounding.CollidableBounding;

import java.util.ArrayList;

public abstract class Collidable {

    protected Material material;
    protected double inverseMass = 0;

    public abstract CollidableBounding getBounding();

    public abstract ArrayList<Contact> contactsWith(Collidable collidable);

    public void render(GL2 gl, GLUT glut) {}

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public double getInverseMass() {
        return inverseMass;
    }

    public boolean immovable() {
        return inverseMass == 0;
    }

    public String getNameString() {
        return getClass().getSimpleName();
    }

    public String getNameIDString() {
        return getNameString();
    }
}
