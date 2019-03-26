package engine;

import engine.collision.Contact;
import engine.collision.bounding.CollidableBounding;

import java.util.ArrayList;

public abstract class Collidable {

    protected Material material;

    public abstract CollidableBounding getBounding();

    public abstract ArrayList<Contact> contactsWith(Collidable collidable);

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public String getNameString() {
        return getClass().getSimpleName();
    }

    public String getNameIDString() {
        return getNameString();
    }
}
