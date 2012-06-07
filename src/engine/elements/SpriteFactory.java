package engine.elements;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.media.opengl.GL;

import engine.buffers.IDrawBuffer;
import engine.drawer.IRender;
import engine.drawer.ShaderInfo;
import engine.drawer.accsessors.IByteBufferAccessor;
import engine.drawer.accsessors.IFloatBufferAccessor;
import engine.elements.factory.IDFactory;

public class SpriteFactory extends IDFactory {

    private static int                   max_count         = 1000;

    private final IRender                m_render;
    private final IDrawBuffer            m_buffer;
    private final int[]                  m_comps           = { 0, 1, 2, 3 };
    private final int[]                  m_indexes;
    private int                          m_lastBufferIndex = 0;

    private Set<Integer>                 m_shown           = new HashSet<Integer>();
    private boolean                      m_updateIndexes   = true;

    protected final IFloatBufferAccessor m_aPos;
    protected final IFloatBufferAccessor m_aRot;
    protected final IFloatBufferAccessor m_aSize;
    protected final IFloatBufferAccessor m_aOrigin;
    protected final IByteBufferAccessor  m_aColor;

    private ShaderInfo                   m_shader;

    public SpriteFactory(IRender render) {

        m_render = render;
        m_buffer = m_render.createQuadsBuffer();
        m_indexes = new int[max_count * 6]; // 6 verts per quad

        setupBuffer();
        setupIndexes();
        setupShader();

        m_aPos = (IFloatBufferAccessor) m_buffer.getAccsessor(1);
        m_aRot = (IFloatBufferAccessor) m_buffer.getAccsessor(1, 2);
        m_aSize = (IFloatBufferAccessor) m_buffer.getAccsessor(2);
        m_aOrigin = (IFloatBufferAccessor) m_buffer.getAccsessor(2, 2);
        m_aColor = (IByteBufferAccessor) m_buffer.getAccsessor(3);

        fillBuffer();
    }

    private void setupShader() {

        m_shader = m_render.loadShader("baseq");
    }

    private void fillBuffer() {

        final float[] quad = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f };

        IFloatBufferAccessor acc = (IFloatBufferAccessor) m_buffer.getAccsessor(0);
        for (int i = 0; i < max_count; i++) {
            acc.putf(i * 8, quad);
        }
    }

    protected int genNewBufferIndex() {
        return m_lastBufferIndex++;
    }

    private void setupBuffer() {

        m_buffer.addComponent(2, GL.GL_FLOAT, max_count, false); // origin
        m_buffer.addComponent(3, GL.GL_FLOAT, max_count, false); // pos rot
        m_buffer.addComponent(4, GL.GL_FLOAT, max_count, false); // size cent
        m_buffer.addComponent(4, GL.GL_UNSIGNED_BYTE, max_count, true); // color
        m_buffer.allocate(GL.GL_DYNAMIC_DRAW);
    }

    private void setupIndexes() {}

    protected void setShow(int idx, boolean state) {

        synchronized (m_shown) {
            if (state) {
                m_shown.add(idx);
            } else {
                m_shown.remove(idx);
            }
        }
        m_updateIndexes = true;
    }

    protected void updateIndexes() {

        Arrays.fill(m_indexes, 0);
        int off = 0;

        synchronized (m_shown) {
            for (int idx : m_shown) {
                m_indexes[off + 0] = idx * 4 + 0;
                m_indexes[off + 1] = idx * 4 + 1;
                m_indexes[off + 2] = idx * 4 + 2;
                m_indexes[off + 3] = idx * 4 + 1;
                m_indexes[off + 4] = idx * 4 + 2;
                m_indexes[off + 5] = idx * 4 + 3;
                off += 6;
            }
        }

        m_updateIndexes = false;
    }

    public void draw() {

        if (m_updateIndexes) updateIndexes();

        m_shader.bind();
        m_shader.uniform("p_matrix", m_render.proj().get());
        m_shader.uniform("v_matrix", m_render.trans().get());

        m_render.drawQuads(m_buffer, m_indexes, 0, m_shown.size() * 6, m_comps);

        m_shader.unbind();
    }

    public Sprite create(int w, int h) {
        Sprite spr = new Sprite(this, genNewBufferIndex());
        register(spr);

        spr.setRect(w, h);
        return spr;
    }
}
