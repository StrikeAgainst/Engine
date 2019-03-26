package engine.collision;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import engine.collision.bounding.Bounding;

public abstract class Contact {

    protected Bounding bounding, otherBounding;

    public Contact(Bounding bounding, Bounding otherBounding) {
        this.bounding = bounding;
        this.otherBounding = otherBounding;
    }

    public void render(GL2 gl, GLUT glut) {}

    public boolean involves(Bounding bounding) {
        return (this.bounding.comprises(bounding) || this.otherBounding.comprises(bounding));
    }
}
