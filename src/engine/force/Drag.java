package engine.force;

import engine.RigidObject;
import engine.Physics;

public class Drag implements ForceGenerator {

    private double linear_damping, angular_damping;

    public Drag() {
        this.linear_damping = Physics.linear_damping;
        this.angular_damping = Physics.angular_damping;
    }

    public Drag(double damping) {
        this.linear_damping = damping;
        this.angular_damping = damping;
    }

    public Drag(double linear_damping, double angular_damping) {
        this.linear_damping = linear_damping;
        this.angular_damping = angular_damping;
    }

    public void updateForce(RigidObject o, double tick) {
        o.getVelocity().scale(Math.pow(linear_damping, tick));
        o.getRotation().scale(Math.pow(angular_damping, tick));
    }
}
