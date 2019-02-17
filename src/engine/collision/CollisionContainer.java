package engine.collision;

import engine.RigidObject;

import java.util.ArrayList;
import java.util.Iterator;

public class CollisionContainer implements Iterable<Collision> {

    private static CollisionContainer container = null;

    private ArrayList<Collision> collisions = new ArrayList<>();

    private CollisionContainer() {}

    public static CollisionContainer get() {
        if (container == null)
            container = new CollisionContainer();

        return container;
    }

    public void add(Collision c) {
        if (!collisions.contains(c))
            collisions.add(c);
    }

    public void add(ArrayList<Collision> cs) {
        for (Collision c : cs)
            add(c);
    }

    public void remove(Collision c) {
        collisions.remove(c);
    }

    public void clear() {
        collisions.clear();
    }

    public boolean contains(Collision c){
        return collisions.contains(c);
    }

    public boolean hasCollisionFor(RigidObject object){
        for (Collision c : collisions)
            if (c.involves(object))
                return true;

        return false;
    }

    public int size() {
        return collisions.size();
    }

    public Iterator<Collision> iterator() {
        return collisions.iterator();
    }

    public String toString() {
        return collisions.toString();
    }

    public String[] toStringArray() {
        int i = 0;
        String[] s = new String[collisions.size()];
        for (Collision c : collisions) {
            s[i++] = c.toString();
        }
        return s;
    }
}
