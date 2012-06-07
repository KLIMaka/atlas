package engine.elements;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import engine.buffers.IDrawBuffer;
import engine.drawer.IRender;
import engine.drawer.ShaderInfo;
import engine.drawer.accsessors.IByteBufferAccessor;
import engine.drawer.accsessors.IFloatBufferAccessor;
import engine.elements.factory.IDFactory;

public class PanelFactory extends IDFactory {

    private static int             max_count = 1000;

    private IRender                m_render;
    private IDrawBuffer            m_buffer;
    private int[]                  m_indexes;
    private final int[]            m_comps   = { 0, 1, 2 };

    private ShaderInfo             m_shader;

    protected IFloatBufferAccessor m_aPos;
    protected IFloatBufferAccessor m_aTex;
    protected IByteBufferAccessor  m_aColor;

    public static class panelElement {
        public int id;
        public int start;
        public int count;
    }

    private Map<Integer, panelElement> m_panels = new HashMap<Integer, panelElement>();

    public PanelFactory(IRender render) {

        m_render = render;
        m_buffer = m_render.createQuadsBuffer();
        m_indexes = new int[max_count * 6]; // 6 verts per quad

        setupBuffer();
        setupIndexes();
        setupShader();

        m_aPos = (IFloatBufferAccessor) m_buffer.getAccsessor(0);
        m_aTex = (IFloatBufferAccessor) m_buffer.getAccsessor(1);
        m_aColor = (IByteBufferAccessor) m_buffer.getAccsessor(2);
    }

    private void setupBuffer() {

        m_buffer.addComponent(2, GL.GL_FLOAT, max_count, false); // pos
        m_buffer.addComponent(2, GL.GL_FLOAT, max_count, false); // texcoord
        m_buffer.addComponent(4, GL.GL_UNSIGNED_BYTE, max_count, true); // color
        m_buffer.allocate(GL2.GL_STREAM_DRAW);
    }

    private void setupIndexes() {

        int off = 0;
        for (int idx = 0; idx < max_count; idx++) {
            m_indexes[off + 0] = idx * 4 + 0;
            m_indexes[off + 1] = idx * 4 + 1;
            m_indexes[off + 2] = idx * 4 + 2;
            m_indexes[off + 3] = idx * 4 + 1;
            m_indexes[off + 4] = idx * 4 + 2;
            m_indexes[off + 5] = idx * 4 + 3;
            off += 6;
        }
    }

    private void setupShader() {
        m_shader = m_render.loadShader("panel");
    }

    public void draw(int[] ids) {

        m_shader.bind();
        m_shader.uniform("p_matrix", m_render.proj().get());
        m_shader.uniform("v_matrix", m_render.trans().get());

        for (int id : ids) {
            panelElement p = m_panels.get(id);
            if (p == null) continue;

            m_render.drawQuads(m_buffer, m_indexes, p.start * 6, p.count * 6, m_comps);
        }

        m_shader.unbind();
    }

}
