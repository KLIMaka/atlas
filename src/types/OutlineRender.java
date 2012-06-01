package types;

import javax.media.opengl.GL2;
import javax.media.opengl.GLES2;

import shaders.Shaders;

import com.jogamp.opengl.util.glsl.ShaderProgram;

public class OutlineRender implements IElementRender {

    private ShaderProgram m_shader = null;

    private GLES2         m_gl;

    private int           m_worldPos;
    private int           m_rect;
    private int           m_scale;

    public OutlineRender(GLES2 gl) {
        m_gl = gl;

        m_shader = Shaders.loadShaderProgram(m_gl, "outline");
        m_rect = m_gl.glGetUniformLocation(m_shader.program(), "rect");
        m_worldPos = m_gl.glGetUniformLocation(m_shader.program(), "worldPos");
        m_scale = m_gl.glGetUniformLocation(m_shader.program(), "scale");

        int s1 = m_gl.glGetUniformLocation(m_shader.program(), "colorTex");

        m_shader.useProgram(m_gl, true);
        m_gl.glUniform1i(s1, 0);
        m_shader.useProgram(m_gl, false);
    }

    public void begin() {
        m_shader.useProgram(m_gl, true);
    }

    public void end() {
        m_shader.useProgram(m_gl, false);
    }

    public void draw(Sprite sprite) {

        m_gl.glUniform3fv(m_worldPos, 1, sprite.worldPos(), 0);
        m_gl.glUniform4fv(m_rect, 1, sprite.rect(), 0);
        m_gl.glUniform1f(m_scale, sprite.scale());

        m_gl.glActiveTexture(GL2.GL_TEXTURE0);
        sprite.heightTex().bind(m_gl);

        // m_gl.glBegin(GL2.GL_QUADS);
        //
        // m_gl.glMultiTexCoord2f(0, 0.0f, 0.0f);
        // m_gl.glVertex2f(0.0f, 0.0f);
        // m_gl.glMultiTexCoord2f(0, 1.0f, 0.0f);
        // m_gl.glVertex2f(1.0f, 0.0f);
        // m_gl.glMultiTexCoord2f(0, 1.0f, 1.0f);
        // m_gl.glVertex2f(1.0f, 1.0f);
        // m_gl.glMultiTexCoord2f(0, 0.0f, 1.0f);
        // m_gl.glVertex2f(0.0f, 1.0f);
        //
        // m_gl.glEnd();

    }

    @Override
    public void draw(DrawableElement elem) {

        if (elem instanceof Sprite) {
            draw((Sprite) elem);
        }
    }
}
