package engine.text;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import engine.buffers.IDrawBuffer;
import engine.drawer.IRender;
import engine.drawer.accsessors.IFloatBufferAccessor;
import engine.text.CircualrHeap.entry;
import engine.text.lines.Caret;
import engine.text.lines.Line;

public class TextRender {

    static final int             max_count    = 2048;

    private FontFileParser       m_font;
    private boolean              m_valid      = false;
    private Texture              m_tex;
    private IRender              m_render;
    private IDrawBuffer          m_buffer;
    private int[]                m_indexes;
    private int[]                m_comps      = { 0, 1, 2 };

    private IFloatBufferAccessor m_aPosSize;
    private IFloatBufferAccessor m_aTex;

    private ShaderProgram        m_shader;
    private int                  m_qvm;
    private int                  m_qpm;
    private int                  m_fcolor;
    private int                  m_bcolor;
    private int                  m_thickness;
    private int                  m_lineHeight;
    private int                  m_curLine;
    private int                  m_startLine;
    private int                  m_clipWindow;

    private CircualrHeap         m_heap       = new CircualrHeap(max_count);
    private boolean              m_needUpdate = true;

    private int                  m_rows       = 35;

    private Line                 m_firstLine  = new Line("");
    private Line                 m_topLine    = m_firstLine;
    private int                  m_topLineNum = 0;
    private Caret                m_caret      = new Caret(m_firstLine);

    public TextRender(String font, IRender render) {
        try {

            m_render = render;
            m_font = new FontFileParser(font);
            String root = new File(font).getParent();
            m_tex = TextureIO.newTexture(new File(root, m_font.getFontImg()), false);
            createBuffers();
            setupShader();

            m_valid = true;

        } catch (IOException e) {
            m_valid = false;
            e.printStackTrace();
        }
    }

    private void setupShader() {

        m_shader = m_render.loadShader("font");
        m_render.bindShader(m_shader);
        int font = m_render.gl().glGetUniformLocation(m_shader.program(), "font");
        m_qvm = m_render.gl().glGetUniformLocation(m_shader.program(), "v_matrix");
        m_qpm = m_render.gl().glGetUniformLocation(m_shader.program(), "p_matrix");
        m_fcolor = m_render.gl().glGetUniformLocation(m_shader.program(), "fcolor");
        m_bcolor = m_render.gl().glGetUniformLocation(m_shader.program(), "bcolor");
        m_thickness = m_render.gl().glGetUniformLocation(m_shader.program(), "thickness");
        m_lineHeight = m_render.gl().glGetUniformLocation(m_shader.program(), "lineHeight");
        m_startLine = m_render.gl().glGetUniformLocation(m_shader.program(), "startLine");
        m_curLine = m_render.gl().glGetUniformLocation(m_shader.program(), "curLine");
        m_clipWindow = m_render.gl().glGetUniformLocation(m_shader.program(), "clipWindow");
        m_render.gl().glUniform1i(font, 0);
        m_render.unbindShader(m_shader);
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

    public void put(char c) {
        m_needUpdate = true;
        m_caret.put(c);
    }

    public void setLineHeight(float h) {
        m_render.bindShader(m_shader);
        m_render.gl().glUniform1f(m_lineHeight, h);
        m_render.unbindShader(m_shader);
    }

    public void setColor(float r, float g, float b, float a) {

        m_render.bindShader(m_shader);
        m_render.gl().glUniform4f(m_fcolor, r, g, b, a);
        m_render.unbindShader(m_shader);
    }

    public void setWindow(float w, float h) {
        m_render.bindShader(m_shader);
        m_render.gl().glUniform2f(m_clipWindow, w, h);
        m_render.unbindShader(m_shader);
        // m_rows = (int) (h / 16) + 1;
        m_needUpdate = true;
    }

    public void setThickness(float t) {

        m_render.bindShader(m_shader);
        m_render.gl().glUniform1f(m_thickness, t);
        m_render.unbindShader(m_shader);
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

    protected void genLine(char[] line, int startIdx, int lunenum) {

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

    private void precache() {

        if (!m_needUpdate) return;

        m_needUpdate = false;
        int ln = m_topLineNum;
        for (String line : m_topLine.limit(m_rows)) {
            entry ent = m_heap.get(line);
            if (ent == null) {
                precacheLine(line, ln);
            }
            ln++;
        }

        if (m_needUpdate) {
            m_buffer.update();
            m_needUpdate = false;
        }

    }

    private void precacheLine(String line, int ln) {

        entry ent = m_heap.allocate(line);
        genLine(line.toCharArray(), ent.start, ln);
        genIndexes(ent.start, ent.length);
        m_needUpdate = true;
    }

    public void draw() {

        precache();

        m_render.bindTexture(m_tex);
        m_render.bindShader(m_shader);
        m_render.gl().glUniformMatrix4fv(m_qpm, 1, true, m_render.proj().get(), 0);
        m_render.gl().glUniformMatrix4fv(m_qvm, 1, true, m_render.trans().get(), 0);
        m_render.gl().glUniform1i(m_startLine, m_topLineNum);
        m_render.gl().glUniform4f(m_bcolor, 0.9f, 0.9f, 0.9f, 1.0f);

        m_render.gl().glEnable(GL.GL_BLEND);
        m_render.gl().glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        int ln = m_topLineNum;
        for (String line : m_topLine.limit(m_rows)) {
            entry ent = m_heap.get(line);
            if (ent == null) continue;

            m_render.gl().glUniform1i(m_curLine, ln++);
            m_render.drawQuads(m_buffer, m_indexes, ent.start * 6, ent.length * 6, m_comps);
        }

        m_render.gl().glDisable(GL.GL_BLEND);
        m_render.unbindShader(m_shader);
    }

    public boolean isValid() {
        return m_valid;
    }

    public void key(KeyEvent e) {
        m_caret.key(e);
        m_needUpdate = true;
    }

}
