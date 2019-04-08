package engine;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import engine.collision.bounding.BroadPhase;

public abstract class SimpleObject extends RigidObject {

	public SimpleObject(Transformation transformation, double mass) {
		this(transformation, mass, InertiaTensorFactory.forDefault(mass));
	}

	public SimpleObject(Transformation transformation, double mass, InertiaTensor inertiaTensor) {
		super(transformation);

		if (mass <= 0) {
			System.out.println("Invalid mass!");
			System.exit(0);
		} else if (mass == Double.POSITIVE_INFINITY) {
			this.inverseMass = 0;
			this.inertiaTensor = InertiaTensor.createImmovable();
		} else {
			this.inverseMass = 1 / mass;
			this.inertiaTensor = inertiaTensor;
			this.inertiaTensor.updateInverseGlobal(transformation);
		}
	}

	protected abstract void draw(GL2 gl, GLUT glut);

	protected abstract void drawTransformed(GL2 gl, GLUT glut);

	public void render(GL2 gl, GLUT glut) {
		if (visible) {
			this.draw(gl, glut);
			if (bounding != null && BroadPhase.VISIBLE)
				bounding.getBroadPhase().render(gl, glut);

			gl.glPushMatrix();
			gl.glMultMatrixd(transformation.getMatrix().getDataLinear(false, true), 0);

			this.drawTransformed(gl, glut);
			if (bounding != null)
				bounding.render(gl, glut);

			gl.glPopMatrix();

		}
	}
}
