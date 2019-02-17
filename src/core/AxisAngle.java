package core;

import engine.Physics;

public class AxisAngle {

    private Vector3 axis;
    private float angle;

    public AxisAngle(Vector3 axis, float angle) {
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

    public void setAngle(float angle) {
        this.angle = Physics.boundAngleFull(angle);
    }

    public float getAngle() {
        return angle;
    }

    public void invert() {
        axis.revert();
    }

    public AxisAngle getInverse() {
        return new AxisAngle(axis.getReverse(), angle);
    }

    public Quaternion toQuaternion() {
        float sin = (float) Math.sin(angle/2), cos = (float) Math.cos(angle/2);
        return new Quaternion(cos, axis.getX()*sin, axis.getY()*sin, axis.getZ()*sin);
    }
}
