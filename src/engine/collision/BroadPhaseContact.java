package engine.collision;

import engine.collision.bounding.BroadPhase;

public class BroadPhaseContact extends Contact {

    public BroadPhaseContact(BroadPhase broadPhase, BroadPhase otherBroadPhase) {
        super(broadPhase, otherBroadPhase);
    }

    public String toString() {
        return "BroadPhaseContact:[broadPhase: "+bounding.getNameString()+", otherBroadPhase: "+otherBounding.getNameString()+"]";
    }
}
