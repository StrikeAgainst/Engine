package core;

import engine.Physics;

public class AxisAngle {

    private Vector3 axis;
    private double angle;

    public AxisAngle(Vector3 axis, double angle) {
        axis.normalize();
        this.axis = axis;
        this.angle = Physics.boundAngleFull(angle);
    }

    public AxisAngle(Vector3 axis) {
        this(axis, 0);
    }

    public void setAxis(Vector3 axis) {
        axis.normalize();
        this.axis = axis;
    }

    public Vector3 getAxis() {
        return axis;
    }

    public void setAngle(double angle) {
        this.angle = Physics.boundAngleFull(angle);
    }

    public double getAngle() {
        return angle;
    }

    public void invert() {
        axis.revert();
    }

    public AxisAngle getInverse() {
        return new AxisAngle(axis.getReverse(), angle);
    }

    public Quaternion toQuaternion() {
        double sin = Math.sin(angle/2), cos = Math.cos(angle/2);
        return new Quaternion(cos, axis.getX()*sin, axis.getY()*sin, axis.getZ()*sin);
    }

    public String toString() {
        return "AxisAngle:[axis:"+axis+", angle:"+Math.toDegrees(angle)+"]";
    }
}
