package engine.elements.factory;

import java.util.HashMap;
import java.util.Map;

import engine.monads.IMonadID;

public class IDFactory {

    protected static Map<Integer, IMonadID> elements = new HashMap<Integer, IMonadID>();
    protected static int                    lastId   = 0;

    protected void register(IMonadID monad) {
        lastId++;
        monad.setID(lastId);
        elements.put(lastId, monad);
    }

    public IMonadID getById(int id) {
        return elements.get(id);
    }
}
