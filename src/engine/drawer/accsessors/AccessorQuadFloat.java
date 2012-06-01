package engine.drawer.accsessors;

import java.nio.FloatBuffer;

public class AccessorQuadFloat implements IFloatBufferAccessor {

    private final FloatBuffer m_buf;
    private final int         m_stride;
    private final int         m_offset;

    public AccessorQuadFloat(FloatBuffer buf, int stride) {
        m_buf = buf;
        m_stride = stride;
        m_offset = 0;
    }

    public AccessorQuadFloat(FloatBuffer buf, int stride, int offset) {
        m_buf = buf;
        m_stride = stride;
        m_offset = offset;
    }

    @Override
    public void put4f(int off, float x, float y, float z, float w) {
        off = off * m_stride * 4 + m_offset;
        for (int i = 0; i < 4 * m_stride; i += m_stride) {
            m_buf.put(i + off + 0, x);
            m_buf.put(i + off + 1, y);
            m_buf.put(i + off + 2, z);
            m_buf.put(i + off + 3, w);
        }
    }

    @Override
    public void put3f(int off, float x, float y, float z) {
        off = off * m_stride * 4 + m_offset;
        for (int i = 0; i < 4 * m_stride; i += m_stride) {
            m_buf.put(i + off + 0, x);
            m_buf.put(i + off + 1, y);
            m_buf.put(i + off + 2, z);
        }
    }

    @Override
    public void put2f(int off, float x, float y) {
        off = off * m_stride * 4 + m_offset;
        for (int i = 0; i < 4 * m_stride; i += m_stride) {
            m_buf.put(i + off + 0, x);
            m_buf.put(i + off + 1, y);
        }
    }

    @Override
    public void put1f(int off, float x) {
        off = off * m_stride * 4 + m_offset;
        for (int i = 0; i < 4 * m_stride; i += m_stride) {
            m_buf.put(i + off + 0, x);
        }
    }

    @Override
    public void putf(int off, float[] data) {
        m_buf.position(off);
        m_buf.put(data, 0, data.length);
        m_buf.rewind();
    }
}
