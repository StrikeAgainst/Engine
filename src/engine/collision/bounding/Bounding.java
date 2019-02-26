package engine.collision.bounding;

public abstract class Bounding {

    public abstract float getXUpperBound();

    public abstract float getXLowerBound();

    public abstract float getYUpperBound();

    public abstract float getYLowerBound();

    public abstract float getZUpperBound();

    public abstract float getZLowerBound();

    public String getNameString() {
        return getClass().getSimpleName();
    }

    public boolean comprises(Bounding bounding) {
        return (this == bounding);
    }

    public abstract String getAttributesString();

    public String getBoundsString() {
        return "XUpper: "+getXUpperBound()+", XLower: "+getXLowerBound()+", YUpper: "+getYUpperBound()+", YLower: "+getYLowerBound()+", ZUpper: "+getZUpperBound()+", ZLower: "+getZLowerBound();
    }

    public String toString() {
        return getNameString()+":["+getAttributesString()+"]";
    }
}
