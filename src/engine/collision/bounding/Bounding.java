package engine.collision.bounding;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public abstract class Bounding {

    public abstract void render(GL2 gl, GLUT glut);

    public abstract float getXUpperBound();

    public abstract float getXLowerBound();

    public abstract float getYUpperBound();

    public abstract float getYLowerBound();

    public abstract float getZUpperBound();

    public abstract float getZLowerBound();

    public String getNameString() {
        return getClass().getSimpleName();
    }

    public abstract String getAttributesString();

    public String getBoundsString() {
        return "XUpper: "+getXUpperBound()+", XLower: "+getXLowerBound()+", YUpper: "+getYUpperBound()+", YLower: "+getYLowerBound()+", ZUpper: "+getZUpperBound()+", ZLower: "+getZLowerBound();
    }

    public String toString() {
        return getNameString()+":["+getAttributesString()+"]";
    }
}
