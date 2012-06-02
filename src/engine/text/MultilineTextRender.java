package engine.text;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import engine.drawer.IRender;
import engine.drawer.ShaderInfo;
import engine.text.lines.Caret;
import engine.text.lines.Line;

public class MultilineTextRender {

    private FontFileParser m_font;
    private Texture        m_tex;

    private IRender        m_render;
    private ShaderInfo     m_shader;
    private TextBuffer     m_textBuffer;

    private int            m_rows       = 35;

    private Line           m_firstLine  = new Line("");
    private Line           m_topLine    = m_firstLine;
    private int            m_topLineNum = 0;
    private Caret          m_caret      = new Caret(m_firstLine);

    public MultilineTextRender(String font, IRender render) {
        try {

            m_render = render;
            m_font = new FontFileParser(font);
            m_textBuffer = new TextBuffer(m_font, m_render);
            String root = new File(font).getParent();
            m_tex = TextureIO.newTexture(new File(root, m_font.getFontImg()), false);
            setupShader();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupShader() {

        m_shader = m_render.loadShader("font");
        m_shader.bind();
        m_shader.uniform("font", 0);
        m_shader.unbind();
    }

    public void setLineHeight(float h) {

        m_shader.bind();
        m_shader.uniform("lineHeight", h);
        m_shader.unbind();
    }

    public void setColor(float r, float g, float b, float a) {

        m_shader.bind();
        m_shader.uniform("fcolor", r, g, b, a);
        m_shader.unbind();
    }

    public void setWindow(float w, float h) {

        m_shader.bind();
        m_shader.uniform("clipWindow", w, h);
        m_shader.unbind();
    }

    public void setThickness(float t) {

        m_shader.bind();
        m_shader.uniform("thickness", t);
        m_shader.unbind();
    }

    public void draw() {

        m_textBuffer.precache(m_topLine.limit(m_rows));

        m_render.bindTexture(m_tex);
        m_shader.bind();
        m_shader.uniform("p_matrix", m_render.proj().get());
        m_shader.uniform("v_matrix", m_render.trans().get());
        m_shader.uniform("startLine", m_topLineNum);
        m_shader.uniform("bcolor", 0.9f, 0.9f, 0.9f, 1.0f);

        m_render.setBlendMode(IRender.BLEND_MODE_TRANS);

        int ln = m_topLineNum;
        for (String line : m_topLine.limit(m_rows)) {

            m_shader.uniform("curLine", ln++);
            m_textBuffer.draw(line);
        }

        m_render.setBlendMode(IRender.MODE_OFF);
        m_shader.unbind();
    }

    public void key(KeyEvent e) {
        m_caret.key(e);
    }

}
