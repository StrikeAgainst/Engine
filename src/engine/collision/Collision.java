package engine.collision;

import engine.RigidObject;
import engine.PhysicalObject;
import core.Vector3;
import engine.collision.bounding.BroadPhase;
import engine.collision.bounding.CollidableBounding;
import engine.collision.bounding.ObjectBounding;

import java.util.ArrayList;

public class Collision {

    protected PhysicalObject object1;
    protected RigidObject object2;
    protected ArrayList<PrimitiveContact> contacts;
    protected BroadPhaseContact broadphaseContact;
    protected Vector3 normal;
    protected float timeFrame, friction, restitution;

    public Collision(PhysicalObject object1, RigidObject object2, ArrayList<PrimitiveContact> contacts, BroadPhaseContact broadphaseContact) {
        this.object1 = object1;
        this.object2 = object2;
        this.contacts = contacts;
        this.broadphaseContact = broadphaseContact;
    }

    public Collision(PhysicalObject object1, RigidObject object2, float timeFrame) {
        this.object1 = object1;
        this.object2 = object2;
        this.timeFrame = timeFrame;
    }

    public boolean involves(RigidObject object) {
        return (object == object1 || object == object2);
    }

    public boolean involves(CollidableBounding bounding) {
        if (bounding instanceof ObjectBounding)
            return involves((ObjectBounding) bounding);
        else if (bounding instanceof BroadPhase)
            return involves((BroadPhase) bounding);

        return false;
    }

    public boolean involves(ObjectBounding bounding) {
        for (PrimitiveContact c : contacts)
            if (c.involves(bounding))
                return true;

        return false;
    }

    public boolean involves(BroadPhase broadphase) {
        return broadphaseContact.involves(broadphase);
    }

    public float getTimeFrame() {
        return timeFrame;
    }

    public float calculateSeparatingVelocity() {
        return 0;
    }

    public void resolve() {

    }

    public String toString() {
        return "Collision:[" + object1.getNameIDString() + " - " + object2.getNameIDString() + ": "+contacts.toString()+"]";
    }
}
