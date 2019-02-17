package engine.collision;

import engine.collision.bounding.CollidableBounding;

public abstract class Contact {

    protected CollidableBounding bounding1, bounding2;

    public Contact(CollidableBounding bounding1, CollidableBounding bounding2) {
        this.bounding1 = bounding1;
        this.bounding2 = bounding2;
    }

    public boolean involves(CollidableBounding bounding) {
        return (bounding == bounding1 || bounding == bounding2);
    }
}
