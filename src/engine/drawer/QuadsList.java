package engine.drawer;

import javax.media.opengl.GL;

import engine.buffers.IDrawBuffer;
import engine.drawer.accsessors.IByteBufferAccessor;
import engine.drawer.accsessors.IFloatBufferAccessor;

public class QuadsList {

    private int                  m_count;
    private IDrawBuffer          m_buffer;
    private int[]                m_idxs;
    private IRender              m_impl;
    private IFloatBufferAccessor m_posRot;
    private int[]                m_coms = { 0, 1, 2 };
    private IByteBufferAccessor  m_size;

    public QuadsList(IRender impl, int count) {
        m_count = count;
        m_buffer = impl.createBuffer();

        m_buffer.addComponent(3, GL.GL_FLOAT, m_count, false);
        m_buffer.addComponent(2, GL.GL_UNSIGNED_BYTE, m_count, false);
        m_buffer.addComponent(4, GL.GL_UNSIGNED_BYTE, m_count, true);
        m_buffer.allocate(GL.GL_DYNAMIC_DRAW);

        m_posRot = (IFloatBufferAccessor) m_buffer.getAccsessor(0, 0);
        m_size = (IByteBufferAccessor) m_buffer.getAccsessor(1, 0);

        m_impl = impl;
        m_idxs = new int[m_count];

        for (int i = 0; i < count; i++) {
            m_idxs[i] = i;
        }

        IByteBufferAccessor acc = (IByteBufferAccessor) m_buffer.getAccsessor(2, 0);
        for (int i = 0; i < m_count; i++) {
            int x = (int) (Math.random() * 256.0);
            acc.put4ub(i * 4, x, 255, 255, 255);
        }
    }

    public void setQuad(int idx, float x, float y, int w, int h, float angle) {
        m_posRot.put3f(idx * 3, x, y, angle);
        m_size.put2ub(idx * 2, w, h);
    }

    public void draw() {
        m_impl.drawSprites(m_buffer, m_idxs, m_coms);
    }
}
