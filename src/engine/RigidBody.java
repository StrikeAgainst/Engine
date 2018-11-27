package engine;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import world.Matrix3x3;
import world.Matrix3x4;
import world.Quaternion;
import world.Point3D;
import world.Vector3D;

public class RigidBody {

    public static int ID_INCREMENT = 0;
    public static boolean SHOW_BOUNDING = false;

    protected int id;
    protected boolean visible = true;

    private static float linear_damping = 0.995f, angular_damping = 0.995f;
    private static Vector3D gravity = new Vector3D(0,0, -9.807f);
    private Point3D position;
    private Vector3D velocity = new Vector3D(), acceleration = new Vector3D(), total_force = new Vector3D(), total_torque = new Vector3D(), rotation  = new Vector3D();
    private Quaternion orientation;
    private Matrix3x3 inverseInertiaTensorLocal, inverseInertiaTensorWorld;
    private Matrix3x4 transformation;
    private float inverseMass;

    public RigidBody(Point3D position, Quaternion orientation, float mass, Matrix3x3 inertiaTensor) {
        this.position = position;
        this.orientation = orientation;

        this.inverseMass = 1/mass;
        this.inverseInertiaTensorLocal = inertiaTensor.getInverse();
    }

    public void calcTransformation() {
        transformation = new Matrix3x4(position, orientation);
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
        //gl.glColor3f(1f,1f,0f);
        //gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        //glut.glutSolidSphere(radius, 12, 4);
        gl.glTranslatef(position.getX()*(-1), position.getY()*(-1), (position.getZ())*(-1));
    }

    public void update(float tick) {
        Vector3D acc = Vector3D.sum(acceleration, Vector3D.product(total_force, inverseMass));
        velocity.add(Vector3D.product(acc, tick));
        velocity.stretch((float) Math.pow(RigidBody.linear_damping, tick));
        position.move(Vector3D.product(velocity, tick));

        Vector3D angular_acc = inverseInertiaTensorWorld.product(total_torque);
        rotation.add(Vector3D.product(angular_acc, tick));
        rotation.stretch((float) Math.pow(RigidBody.angular_damping, tick));
        orientation.add(Vector3D.product(rotation, tick));
        orientation.normalize();

        calcTransformation();
        transformInertiaTensor();

        clearForce();
        clearTorque();
    }

    public void applyForce(Vector3D force) {
        total_force.add(force);
    }

    public void applyForceLocal(Vector3D force, Point3D p) {
        applyForceWorld(force, transformation.product(p));
    }

    public void applyForceWorld(Vector3D force, Point3D p) {
        total_force.add(force);
        total_torque.add(Vector3D.cross(force, new Vector3D(position, p)));
    }

    public void clearForce() {
        total_force.nullify();
    }

    public void clearTorque() {
        total_torque.nullify();
    }

    public Point3D getPosition() {
        return position;
    }

    public void setPosition(Point3D position) {
        this.position = position;
    }

    public Vector3D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3D velocity) {
        this.velocity = velocity;
    }

    public Vector3D getAcceleration() {
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

    public static Vector3D getGravity() {
        return gravity;
    }

}
