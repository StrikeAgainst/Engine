package engine.collision.bounding;

import engine.collision.Contact;
import engine.collision.ContactContainer;
import engine.collision.ContactProperties;

import java.util.ArrayList;

public abstract class CollidableBounding extends Bounding {

    public boolean inContact() {
        for (Contact c : ContactContainer.get())
            if (c.involves(this))
                return true;

        return false;
    }

    public abstract ArrayList<ContactProperties> contactsWith(CollidableBounding bounding);
}
