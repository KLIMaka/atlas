package types.renders;

import javax.media.opengl.GL2;
import javax.media.opengl.GLES2;

import shaders.Shaders;
import types.DrawableElement;
import types.IElementRender;

import com.jogamp.opengl.util.glsl.ShaderProgram;

public abstract class Render implements IElementRender {

    private ShaderProgram m_shader;
    private int[]         m_texCoordAttribs;
    private int[]         m_texIndexes;
    private GLES2         m_gl;

    public Render(GLES2 gl, String shader, int[] tca, String[] txn, int[] txi) {

        m_gl = gl;
        m_texCoordAttribs = tca;
        m_texIndexes = txi;

        m_shader = Shaders.loadShaderProgram(gl(), shader);
        int[] tx = new int[txn.length];
        for (int i = 0; i < tx.length; i++) {
            tx[i] = gl().glGetUniformLocation(m_shader.program(), txn[i]);
        }
        m_shader.useProgram(gl(), true);
        for (int i = 0; i < tx.length; i++) {
            gl().glUniform1i(tx[i], i);
        }
        m_shader.useProgram(gl(), false);
    }

    protected ShaderProgram getShader() {
        return m_shader;
    }

    protected GLES2 gl() {
        return m_gl;
    }

    public void begin() {
        m_shader.useProgram(gl(), true);
    }

    public void end() {
        m_shader.useProgram(gl(), false);
    }

    public void prepare(DrawableElement elem) {

        for (int i = 0; i < m_texCoordAttribs.length; i++) {
            gl().glVertexAttrib4fv(2 + i, elem.getAttrib(m_texCoordAttribs[i]), 0);
        }
        for (int i = 0; i < m_texIndexes.length; i++) {
            gl().glActiveTexture(GL2.GL_TEXTURE0 + i);
            elem.getTex(m_texIndexes[i]).bind(gl());
        }

    }

    public abstract void draw(DrawableElement elem);

}
