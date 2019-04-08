package engine.force;

import core.Point3;
import core.Vector3;
import engine.RigidObject;
import engine.Player;

public class PickedForce implements ForceGenerator {

    private Player player;

    public PickedForce(Player player) {
        this.player = player;
    }

    public void updateForce(RigidObject o, double tick) {
        Point3 pickedPoint = player.getPickedPoint();
        Vector3 movement = Vector3.offset(o.getPosition(), pickedPoint);
        o.setVelocity(movement.scaled(2));
    }
}
