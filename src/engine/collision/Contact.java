package engine.collision;

import engine.collision.bounding.Bounding;

public abstract class Contact {

    protected Bounding bounding, otherBounding;

    public Contact(Bounding bounding, Bounding otherBounding) {
        this.bounding = bounding;
        this.otherBounding = otherBounding;
    }

    public boolean involves(Bounding bounding) {
        return (this.bounding.comprises(bounding) || this.otherBounding.comprises(bounding));
    }
}
