package engine.drawer;

import javax.media.opengl.GL;

import engine.buffers.IDrawBuffer;
import engine.drawer.accsessors.IByteBufferAccessor;
import engine.drawer.accsessors.IFloatBufferAccessor;

public class QuadsList1 {

    private final int                 m_count;
    private final IDrawBuffer         m_buffer;
    private final int[]               m_idxs;
    private final IRender             m_impl;
    private final int[]               m_comps = { 0, 1, 2, 3 };
    private final IByteBufferAccessor m_size;
    private IFloatBufferAccessor      m_pos;

    public QuadsList1(IRender impl, int count) {
        m_count = count;
        m_buffer = impl.createQuadsBuffer();

        m_buffer.addComponent(2, GL.GL_FLOAT, m_count, false);
        m_buffer.addComponent(3, GL.GL_FLOAT, m_count, false);
        m_buffer.addComponent(2, GL.GL_UNSIGNED_BYTE, m_count, false);
        m_buffer.addComponent(4, GL.GL_UNSIGNED_BYTE, m_count, true);
        m_buffer.allocate(GL.GL_DYNAMIC_DRAW);

        m_pos = (IFloatBufferAccessor) m_buffer.getAccsessor(1);
        m_size = (IByteBufferAccessor) m_buffer.getAccsessor(2);

        m_impl = impl;
        m_idxs = new int[m_count * 6];

        for (int i = 0; i < count; i++) {
            m_idxs[i * 6 + 0] = i * 4 + 0;
            m_idxs[i * 6 + 1] = i * 4 + 1;
            m_idxs[i * 6 + 2] = i * 4 + 2;
            m_idxs[i * 6 + 3] = i * 4 + 1;
            m_idxs[i * 6 + 4] = i * 4 + 2;
            m_idxs[i * 6 + 5] = i * 4 + 3;
        }

        IByteBufferAccessor acc = (IByteBufferAccessor) m_buffer.getAccsessor(3);
        for (int i = 0; i < m_count; i++) {
            int x = (int) (Math.random() * 256.0);
            acc.put4ub(i, x, x, x, 255);
        }

        final float[] quad = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f };
        IFloatBufferAccessor acc1 = (IFloatBufferAccessor) m_buffer.getAccsessor(0);
        for (int i = 0; i < m_count; i++) {
            acc1.putf(i * 8, quad);
        }
    }

    public void setQuad(int idx, float x, float y, int w, int h, float angle) {

        if (angle != Float.MAX_VALUE) {
            m_pos.put3f(idx, x, y, angle);
        } else {
            m_pos.put2f(idx, x, y);
        }

        if (w != -1 && h != -1) m_size.put2ub(idx, w, h);
    }

    public void draw() {
        m_impl.drawQuads(m_buffer, m_idxs, 0, m_idxs.length, m_comps);
    }
}
