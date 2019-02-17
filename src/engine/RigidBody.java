package engine;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import core.*;

public class RigidBody {

    private static float linear_damping = 0.995f, angular_damping = 0.995f;
    private static Vector3 gravity = new Vector3(0,0, -9.807f);
    private Point3 position;
    private Vector3 velocity = new Vector3(), acceleration = new Vector3(), total_force = new Vector3(), total_torque = new Vector3(), rotation  = new Vector3();
    private Quaternion orientation;
    private Matrix3x3 inverseInertiaTensorLocal, inverseInertiaTensorWorld;
    private Matrix3x4 transformation;
    private float inverseMass;

    public RigidBody(Point3 position, Quaternion orientation, float mass, Matrix3x3 inertiaTensor) {
        this.position = position;
        this.orientation = orientation;

        this.inverseMass = 1/mass;
        this.inverseInertiaTensorLocal = inertiaTensor.getInverse();
    }

    public void calcTransformation() {
        float x = position.getX(), y = position.getY(), z = position.getZ();
        float r = orientation.getR(), i = orientation.getI(), j = orientation.getJ(), k = orientation.getK();

        float ir2 = 2*i*r, ii2 = 2*i*i, ij2 = 2*i*j, ik2 = 2*i*k, jr2 = 2*j*r, jj2 = 2*j*j, jk2 = 2*j*k, kr2 = 2*k*r, kk2 = 2*k*k;
        float[][] data = new float[][] {
                {
                        1 - (jj2 + kk2),
                        ij2 + kr2,
                        ik2 - jr2
                },
                {
                        ij2 - kr2,
                        1 - (ii2 + kk2),
                        jk2 + ir2
                },
                {
                        ik2 + jr2,
                        jk2 - ir2,
                        1 - (ii2 + jj2)
                },
                {
                        x,
                        y,
                        z
                }
        };

        transformation = new Matrix3x4(data);
    }

    public void transformInertiaTensor() {
        float[][] tData = transformation.getData();
        float[][] itlData = inverseInertiaTensorLocal.getData();

        float t00 = tData[0][0]*itlData[0][0] + tData[1][0]*itlData[0][1] + tData[2][0]*itlData[0][2];
        float t10 = tData[0][0]*itlData[1][0] + tData[1][0]*itlData[1][1] + tData[2][0]*itlData[1][2];
        float t20 = tData[0][0]*itlData[2][0] + tData[1][0]*itlData[2][1] + tData[2][0]*itlData[2][2];

        float t01 = tData[0][1]*itlData[0][0] + tData[1][1]*itlData[0][1] + tData[2][1]*itlData[0][2];
        float t11 = tData[0][1]*itlData[1][0] + tData[1][1]*itlData[1][1] + tData[2][1]*itlData[1][2];
        float t21 = tData[0][1]*itlData[2][0] + tData[1][1]*itlData[2][1] + tData[2][1]*itlData[2][2];

        float t02 = tData[0][2]*itlData[0][0] + tData[1][2]*itlData[0][1] + tData[2][2]*itlData[0][2];
        float t12 = tData[0][2]*itlData[1][0] + tData[1][2]*itlData[1][1] + tData[2][2]*itlData[1][2];
        float t22 = tData[0][2]*itlData[2][0] + tData[1][2]*itlData[2][1] + tData[2][2]*itlData[2][2];

        inverseInertiaTensorWorld = new Matrix3x3(new float[][] {
                {
                    t00*tData[0][0] + t10*tData[1][0] + t20*tData[2][0],
                    t01*tData[0][0] + t11*tData[1][0] + t21*tData[2][0],
                    t02*tData[0][0] + t12*tData[1][0] + t22*tData[2][0]
                },
                {
                    t00*tData[0][1] + t10*tData[1][1] + t20*tData[2][1],
                    t01*tData[0][1] + t11*tData[1][1] + t21*tData[2][1],
                    t02*tData[0][1] + t12*tData[1][1] + t22*tData[2][1]
                },
                {
                    t00*tData[0][2] + t10*tData[1][2] + t20*tData[2][2],
                    t01*tData[0][2] + t11*tData[1][2] + t21*tData[2][2],
                    t02*tData[0][2] + t12*tData[1][2] + t22*tData[2][2]
                }
        });
    }

    public void draw(GL2 gl, GLUT glut) {
        gl.glTranslatef(position.getX(), position.getY(), position.getZ());
        gl.glColor3f(1f,1f,0f);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        glut.glutSolidSphere(1, 12, 4);
        gl.glTranslatef(position.getX()*(-1), position.getY()*(-1), (position.getZ())*(-1));
    }

    public void update(float tick) {
        Vector3 acc = Vector3.sum(acceleration, total_force.scaled(inverseMass));
        velocity.add(acc.scaled(tick));
        velocity.scale((float) Math.pow(RigidBody.linear_damping, tick));
        position.add(velocity.scaled(tick));

        Vector3 angular_acc = inverseInertiaTensorWorld.product(total_torque);
        rotation.add(angular_acc.scaled(tick));
        rotation.scale((float) Math.pow(RigidBody.angular_damping, tick));
        orientation.add(rotation.scaled(tick));
        orientation.normalize();

        calcTransformation();
        transformInertiaTensor();

        clearForce();
        clearTorque();
    }

    public void applyForce(Vector3 force) {
        total_force.add(force);
    }

    public void applyForceLocal(Vector3 force, Point3 p) {
        applyForceWorld(force, transformation.product(p));
    }

    public void applyForceWorld(Vector3 force, Point3 p) {
        total_force.add(force);
        total_torque.add(Vector3.cross(force, Vector3.offset(position, p)));
    }

    public void clearForce() {
        total_force.nullify();
    }

    public void clearTorque() {
        total_torque.nullify();
    }

    public Point3 getPosition() {
        return position;
    }

    public void setPosition(Point3 position) {
        this.position = position;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }

    public Vector3 getAcceleration() {
        return acceleration;
    }

    public float getMass() {
        return 1/inverseMass;
    }

    public float getInverseMass() {
        return inverseMass;
    }

    public boolean hasFiniteMass() {
        return inverseMass > 0;
    }

    public static Vector3 getGravity() {
        return gravity;
    }

}
