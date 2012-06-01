package engine.text;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CircualrHeap {

    public static class entry {
        public int    length;
        public int    start;
        public String id;
    }

    private final int                 m_size;
    private final Map<Integer, entry> m_blocks     = new HashMap<Integer, entry>();
    private final Map<String, entry>  m_blocksById = new TreeMap<String, entry>();
    private int                       m_freeStart;
    private int                       m_freeEnd;

    public CircualrHeap(int size) {
        m_size = size;
        m_freeStart = 0;
        m_freeEnd = m_size;
    }

    protected entry addEntry(String id, int size) {
        entry ent = new entry();
        ent.start = m_freeStart;
        ent.length = size;
        ent.id = id;
        m_freeStart += size;
        if (ent.length != 0) {
            m_blocks.put(ent.start, ent);
        }
        m_blocksById.put(id, ent);
        return ent;
    }

    public entry allocate(String str) {
        int size = str.length() + 1;
        if (m_freeStart + size <= m_freeEnd) {
            return addEntry(str, size);
        } else {
            if (free(size)) {
                return addEntry(str, size);
            } else {
                return null;
            }
        }
    }

    public void clear() {
        m_blocks.clear();
        m_blocksById.clear();
        m_freeStart = 0;
        m_freeEnd = m_size;
    }

    protected boolean free(int size) {

        int need = size - (m_freeStart - m_freeEnd);
        entry ent = m_blocks.get(m_freeEnd);
        while (need > 0 && ent != null) {
            need -= ent.length;
            m_blocks.remove(m_freeEnd);
            m_blocksById.remove(ent.id);
            m_freeEnd += ent.length;
            ent = m_blocks.get(m_freeEnd);
            if (ent == null) {
                m_freeEnd = m_size;
            }
        }

        if (need <= 0) {
            return true;
        } else if (m_freeStart == 0) {
            return false;
        }

        m_freeStart = 0;
        m_freeEnd = 0;
        return free(size);
    }

    public entry get(String id) {
        return m_blocksById.get(id);
    }

    @Override
    public String toString() {
        return "" + m_freeStart + " - " + m_freeEnd;
    }

}
