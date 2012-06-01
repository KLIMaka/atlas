package engine.drawer.accsessors;

import java.nio.ByteBuffer;

public class AccessorUByte implements IByteBufferAccessor {

    private ByteBuffer m_buf;

    public AccessorUByte(ByteBuffer buf) {
        m_buf = buf;
    }

    @Override
    public void put4ub(int off, int x, int y, int z, int w) {
        m_buf.put(off + 0, (byte) x);
        m_buf.put(off + 1, (byte) y);
        m_buf.put(off + 2, (byte) z);
        m_buf.put(off + 3, (byte) w);
    }

    @Override
    public void put1ub(int off, int x) {
        m_buf.put(off, (byte) x);
    }

    @Override
    public void put2ub(int off, int x, int y) {
        m_buf.put(off + 0, (byte) x);
        m_buf.put(off + 1, (byte) y);
    }

    @Override
    public void put3ub(int off, int x, int y, int z) {
        m_buf.put(off + 0, (byte) x);
        m_buf.put(off + 1, (byte) y);
        m_buf.put(off + 2, (byte) z);

    }

    @Override
    public void putub(int off, byte[] data) {
        m_buf.put(data, off, data.length);
        m_buf.rewind();
    }

}
