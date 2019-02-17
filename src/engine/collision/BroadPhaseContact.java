package engine.collision;

import engine.collision.bounding.BroadPhase;

public class BroadPhaseContact extends Contact {

    public BroadPhaseContact(BroadPhase bounding1, BroadPhase bounding2) {
        super(bounding1, bounding2);
    }

    public String toString() {
        return "BroadPhaseContact:[bounding1: "+bounding1.getNameString()+", bounding2: "+bounding2.getNameString()+"]";
    }
}
