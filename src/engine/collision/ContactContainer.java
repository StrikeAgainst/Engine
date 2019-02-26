package engine.collision;

import java.util.ArrayList;
import java.util.Iterator;

public class ContactContainer implements Iterable<Contact> {

    private static ContactContainer container = null;

    private ArrayList<Contact> contacts = new ArrayList<>();

    private ContactContainer() {}

    public static ContactContainer get() {
        if (container == null)
            container = new ContactContainer();

        return container;
    }

    public ArrayList<Contact> getAll() {
        return new ArrayList<>(contacts);
    }

    public void add(Contact c) {
        if (!contacts.contains(c))
            contacts.add(c);
    }

    public void add(ArrayList<Contact> cs) {
        for (Contact c : cs)
            add(c);
    }

    public void remove(Contact c) {
        contacts.remove(c);
    }

    public void clear() {
        contacts.clear();
    }

    public boolean contains(Contact c){
        return contacts.contains(c);
    }

    public int size() {
        return contacts.size();
    }

    public Iterator<Contact> iterator() {
        return contacts.iterator();
    }

    public String toString() {
        return contacts.toString();
    }

    public String[] toStringArray() {
        int i = 0;
        String[] s = new String[contacts.size()];
        for (Contact c : contacts) {
            s[i++] = c.toString();
        }
        return s;
    }
}
