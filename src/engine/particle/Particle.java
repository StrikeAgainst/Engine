package engine.particle;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import world.Point3D;
import world.Vector3D;

public class Particle {

    private static float damping = 0.995f;
    private static Vector3D gravity = new Vector3D(0,0, -9.807f);
    private Point3D position;
    private Vector3D velocity = new Vector3D(), acceleration = new Vector3D(), total_force = new Vector3D();
    private float inverseMass;

    public Particle(Point3D position, float inverseMass) {
        this.position = position;
        this.inverseMass = inverseMass;
    }

    public void draw(GL2 gl, GLUT glut) {
        gl.glTranslatef(position.getX(), position.getY(), position.getZ());
        gl.glColor3f(1f,1f,0f);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        glut.glutSolidSphere(0.1f, 12, 4);
        gl.glTranslatef(position.getX()*(-1), position.getY()*(-1), (position.getZ())*(-1));
    }

    public void update(float tick) {
        position.move(Vector3D.product(velocity, tick));
        Vector3D acc2 = Vector3D.sum(acceleration, Vector3D.product(total_force, inverseMass));
        velocity.add(Vector3D.product(acc2, tick));
        velocity.stretch((float) Math.pow(Particle.damping, tick));
        clearForce();
    }

    public void applyForce(Vector3D force) {
        total_force.add(force);
    }

    public void clearForce() {
        total_force.nullify();
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
