package engine.text;

import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import engine.drawer.IRender;
import engine.drawer.ShaderInfo;

public class TextRender {

    private IRender        m_render;
    private FontFileParser m_font;
    private TextBuffer     m_textBuffer;
    private Texture        m_tex;
    private ShaderInfo     m_shader;

    public TextRender(String font, IRender render) {
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

    public void drawLine(String line) {

        m_textBuffer.precache(line);

        m_render.bindTexture(m_tex);
        m_shader.bind();
        m_shader.uniform("p_matrix", m_render.proj().get());
        m_shader.uniform("v_matrix", m_render.trans().get());
        m_shader.uniform("startLine", 0);
        m_shader.uniform("bcolor", 0.9f, 0.9f, 0.9f, 1.0f);
        m_shader.uniform("curLine", 0);

        m_render.setBlendMode(IRender.BLEND_MODE_TRANS);
        m_textBuffer.draw(line);

        m_render.setBlendMode(IRender.MODE_OFF);
        m_shader.unbind();
    }

}
