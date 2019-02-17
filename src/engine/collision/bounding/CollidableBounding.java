package engine.collision.bounding;

import engine.collision.Collision;
import engine.collision.CollisionContainer;

import java.util.ArrayList;

public abstract class CollidableBounding extends Bounding {

    protected ArrayList<Collision> collisions;
    protected boolean colliding;

    public boolean inContact() {
        for (Collision c : CollisionContainer.get())
            if (c.involves(this))
                return true;

        return false;
    }
}
