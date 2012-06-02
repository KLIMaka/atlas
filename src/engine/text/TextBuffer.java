package engine.text;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import engine.buffers.IDrawBuffer;
import engine.drawer.IRender;
import engine.drawer.accsessors.IFloatBufferAccessor;
import engine.text.CircualrHeap.entry;

public class TextBuffer {

    static final int             max_count    = 2048;

    private IRender              m_render;
    private FontFileParser       m_font;

    private IDrawBuffer          m_buffer;
    private int[]                m_indexes;
    private int[]                m_comps      = { 0, 1, 2 };

    private CircualrHeap         m_heap       = new CircualrHeap(max_count);
    private boolean              m_needUpdate = true;

    private IFloatBufferAccessor m_aPosSize;
    private IFloatBufferAccessor m_aTex;

    public TextBuffer(FontFileParser font, IRender render) {

        m_render = render;
        m_font = font;
        createBuffers();
    }

    private void createBuffers() {

        m_buffer = m_render.createQuadsBuffer();
        m_indexes = new int[max_count * 6];

        m_buffer.addComponent(2, GL.GL_FLOAT, max_count, false); // origin
        m_buffer.addComponent(4, GL.GL_FLOAT, max_count, false); // pos size
        m_buffer.addComponent(2, GL.GL_FLOAT, max_count, false); // texcoords
        m_buffer.allocate(GL2.GL_STREAM_DRAW);

        final float[] quad = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f };
        IFloatBufferAccessor acc = (IFloatBufferAccessor) m_buffer.getAccsessor(0);
        for (int i = 0; i < max_count; i++) {
            acc.putf(i * 8, quad);
        }

        m_aPosSize = (IFloatBufferAccessor) m_buffer.getAccsessor(1);
        m_aTex = (IFloatBufferAccessor) m_buffer.getAccsessor(2);
    }

    protected void genIndexes(int start, int count) {

        int off = start * 6;
        for (int i = start; count > 0; count--, i++, off += 6) {
            m_indexes[off + 0] = i * 4 + 0;
            m_indexes[off + 1] = i * 4 + 1;
            m_indexes[off + 2] = i * 4 + 2;
            m_indexes[off + 3] = i * 4 + 1;
            m_indexes[off + 4] = i * 4 + 2;
            m_indexes[off + 5] = i * 4 + 3;;
        }
    }

    protected void genLine(char[] line, int startIdx) {

        float x = 0.0f;
        for (int i = 0; i < line.length; i++) {
            charStruct cs = m_font.getChar(line[i]);
            m_aPosSize.put4f(i + startIdx, x + cs.xOff, 0.0f + cs.yOff, cs.w, cs.h);
            m_aTex.putf((i + startIdx) * 8, cs.texCoords);
            x += cs.xAdv;
        }

        charStruct space = m_font.getChar(' ');
        m_aPosSize.put4f(line.length + startIdx, x + space.xOff, 0.0f + space.yOff, space.w * 1024.0f, space.h);
        m_aTex.putf((line.length + startIdx) * 8, space.texCoords);

    }

    public void precache(Iterable<String> lines) {

        m_needUpdate = false;
        for (String line : lines) {
            entry ent = m_heap.get(line);
            if (ent == null) {
                precacheLine(line);
            }
        }

        if (m_needUpdate) {
            m_buffer.update();
            m_needUpdate = false;
        }
    }

    public void precache(String line) {

        entry ent = m_heap.get(line);
        if (ent == null) {
            precacheLine(line);
            m_buffer.update();
        }
    }

    private void precacheLine(String line) {

        entry ent = m_heap.allocate(line);
        genLine(line.toCharArray(), ent.start);
        genIndexes(ent.start, ent.length);
        m_needUpdate = true;
    }

    public void draw(String str) {

        entry ent = m_heap.get(str);
        if (ent == null) return;

        m_render.drawQuads(m_buffer, m_indexes, ent.start * 6, ent.length * 6, m_comps);
    }

}
