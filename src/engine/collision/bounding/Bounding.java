package engine.collision.bounding;

public abstract class Bounding {

    public abstract double getXUpperBound();

    public abstract double getXLowerBound();

    public abstract double getYUpperBound();

    public abstract double getYLowerBound();

    public abstract double getZUpperBound();

    public abstract double getZLowerBound();

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
