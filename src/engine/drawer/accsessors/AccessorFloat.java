package engine.drawer.accsessors;

import java.nio.FloatBuffer;

public class AccessorFloat implements IFloatBufferAccessor {

    private final FloatBuffer m_buf;

    public AccessorFloat(FloatBuffer buf) {
        m_buf = buf;
    }

    @Override
    public void put4f(int off, float x, float y, float z, float w) {
        m_buf.put(off + 0, x);
        m_buf.put(off + 1, y);
        m_buf.put(off + 2, z);
        m_buf.put(off + 3, w);
    }

    @Override
    public void put3f(int off, float x, float y, float z) {
        m_buf.put(off + 0, x);
        m_buf.put(off + 1, y);
        m_buf.put(off + 2, z);
    }

    @Override
    public void put2f(int off, float x, float y) {
        m_buf.put(off + 0, x);
        m_buf.put(off + 1, y);
    }

    @Override
    public void put1f(int off, float x) {
        m_buf.put(off + 0, x);
    }

    @Override
    public void putf(int off, float[] data) {
        m_buf.put(data, off, data.length);
        m_buf.rewind();
    }
}
