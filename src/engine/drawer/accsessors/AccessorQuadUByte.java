package engine.drawer.accsessors;

import java.nio.ByteBuffer;

public class AccessorQuadUByte implements IByteBufferAccessor {

    private final ByteBuffer m_buf;
    private final int        m_stride;
    private final int        m_offset;

    public AccessorQuadUByte(ByteBuffer buf, int stride) {
        m_buf = buf;
        m_stride = stride;
        m_offset = 0;
    }

    public AccessorQuadUByte(ByteBuffer buf, int stride, int offset) {
        m_buf = buf;
        m_stride = stride;
        m_offset = offset;

    }

    @Override
    public void put1ub(int off, int x) {
        off = off * m_stride * 4 + m_offset;
        for (int i = 0; i < 4 * m_stride; i += m_stride) {
            m_buf.put(i + off + 0, (byte) x);
        }
    }

    @Override
    public void put2ub(int off, int x, int y) {
        off = off * m_stride * 4 + m_offset;
        for (int i = 0; i < 4 * m_stride; i += m_stride) {
            m_buf.put(i + off + 0, (byte) x);
            m_buf.put(i + off + 1, (byte) y);
        }
    }

    @Override
    public void put3ub(int off, int x, int y, int z) {
        off = off * m_stride * 4 + m_offset;
        for (int i = 0; i < 4 * m_stride; i += m_stride) {
            m_buf.put(i + off + 0, (byte) x);
            m_buf.put(i + off + 1, (byte) y);
            m_buf.put(i + off + 2, (byte) z);
        }
    }

    @Override
    public void put4ub(int off, int x, int y, int z, int w) {
        off = off * m_stride * 4 + m_offset;
        for (int i = 0; i < 4 * m_stride; i += m_stride) {
            m_buf.put(i + off + 0, (byte) x);
            m_buf.put(i + off + 1, (byte) y);
            m_buf.put(i + off + 2, (byte) z);
            m_buf.put(i + off + 3, (byte) w);
        }
    }

    @Override
    public void putub(int off, byte[] data) {
        m_buf.put(data, off, data.length);
        m_buf.rewind();
    }

}
