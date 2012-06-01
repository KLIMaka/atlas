package types;

import java.util.ArrayList;

public class ElementsFactory {

    static protected int                        lastID   = 1;
    static protected ArrayList<DrawableElement> elements = new ArrayList<DrawableElement>();

    static public DrawableElement get(int id) {
        if (id > 0 && id <= elements.size()) {
            return elements.get(id - 1);
        } else {
            return null;
        }
    }

    static protected synchronized int put(DrawableElement el) {
        elements.add(el);
        el.setID(lastID++);
        return lastID;
    }

}
