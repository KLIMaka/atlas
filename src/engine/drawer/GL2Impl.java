package engine.drawer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;

import shaders.Shaders;

import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.texture.Texture;

import engine.buffers.IDrawBuffer;
import engine.buffers.bufferConfig;
import engine.drawer.accsessors.IFloatBufferAccessor;
import engine.transformer.DefaultTransformer;
import engine.transformer.ITransformer3D;
import gleem.linalg.Vec4f;

public class GL2Impl implements IRender {

    private GL2                   m_gl;

    private boolean               m_depthTest     = false;
    private boolean               m_stencilTest   = false;
    private boolean               m_blend         = false;

    private int                   m_currentBuffer = -1;

    private int                   m_stencilRef;

    private PlanarComponentBuffer m_stdBuffer;
    private ShaderProgram         m_prog;
    private int                   m_mvp;
    private ShaderProgram         m_qprog;
    private int                   m_qvm;
    private int                   m_qpm;

    private DefaultTransformer    m_projection    = new DefaultTransformer();
    private ITransformer3D        m_transformer   = new DefaultTransformer();

    public void setGL(GL2ES2 gl) {
        m_gl = gl.getGL2();
        init();
    }

    protected void init() {

        m_stdBuffer = new PlanarComponentBuffer(this);
        m_stdBuffer.addComponent(2, GL.GL_FLOAT, 4, false);
        m_stdBuffer.allocate(GL.GL_DYNAMIC_DRAW);

        m_prog = Shaders.loadShaderProgram(m_gl, "base");
        m_mvp = m_gl.glGetUniformLocation(m_prog.program(), "mvp_matrix");

        m_qprog = Shaders.loadShaderProgram(m_gl, "baseq");
        m_qvm = m_gl.glGetUniformLocation(m_qprog.program(), "v_matrix");
        m_qpm = m_gl.glGetUniformLocation(m_qprog.program(), "p_matrix");
    }

    public GL2ES2 gl() {
        return m_gl;
    }

    @Override
    public ShaderProgram loadShader(String name) {
        return Shaders.loadShaderProgram(m_gl, name);
    }

    @Override
    public void bindTexture(Texture tex) {
        tex.bind(m_gl);
    }

    @Override
    public void unbindTexture(Texture tex) {}

    @Override
    public void bindShader(ShaderProgram shader) {
        shader.useProgram(m_gl, true);
    }

    @Override
    public void unbindShader(ShaderProgram shader) {
        shader.useProgram(m_gl, false);
    }

    public void drawSprites(IDrawBuffer buf, int[] idxs, int[] comps) {

        buf.bind();

        int i = 0;
        List<bufferConfig> cfgs = buf.getConfig();
        for (int c : comps) {
            bufferConfig cfg = cfgs.get(c);
            m_gl.glVertexAttribPointer(i, cfg.size, cfg.type, cfg.normalize, cfg.stride, cfg.offset);
            m_gl.glEnableVertexAttribArray(i);
            i++;
        }

        m_gl.glEnable(GL2.GL_VERTEX_PROGRAM_POINT_SIZE);
        m_gl.glEnable(GL2.GL_POINT_SPRITE);

        m_prog.useProgram(m_gl, true);

        m_gl.glUniformMatrix4fv(m_mvp, 1, true, m_transformer.get(), 0);

        m_gl.glDrawElements(GL.GL_POINTS, idxs.length, GL2.GL_UNSIGNED_INT, IntBuffer.wrap(idxs));

        m_prog.useProgram(m_gl, false);
    }

    @Override
    public void drawQuads(IDrawBuffer buf, int[] idxs, int off, int count, int[] comps) {

        buf.bind();

        int i = 0;
        List<bufferConfig> cfgs = buf.getConfig();
        for (int c : comps) {
            bufferConfig cfg = cfgs.get(c);
            m_gl.glVertexAttribPointer(i, cfg.size, cfg.type, cfg.normalize, cfg.stride, cfg.offset);
            m_gl.glEnableVertexAttribArray(i);
            i++;
        }

        m_gl.glDrawElements(GL.GL_TRIANGLES, count, GL2.GL_UNSIGNED_INT, IntBuffer.wrap(idxs, off, count));
    }

    public void bindBuffer(int idx) {
        if (m_currentBuffer != idx) {
            m_gl.glBindBuffer(GL.GL_ARRAY_BUFFER, idx);
        }
    }

    public void updateBuffer(int offset, int length, Buffer data) {
        m_gl.glBufferSubData(GL.GL_ARRAY_BUFFER, offset, length, data);
    }

    static ByteBuffer rectIndexes = ByteBuffer.wrap(new byte[] { 0, 1, 2, 1, 2, 3 });

    public void drawRect(float left, float top, float right, float bottom) {

        float[] buf = new float[] { left, top, right, top, left, bottom, right, bottom };
        ((IFloatBufferAccessor) m_stdBuffer.getAccsessor(0)).putf(0, buf);
        m_stdBuffer.bind();
        m_stdBuffer.update();

        m_gl.glVertexAttribPointer(0, 2, GL.GL_FLOAT, false, 0, 0);
        m_gl.glEnableVertexAttribArray(0);
        m_qprog.useProgram(m_gl, true);
        m_gl.glUniformMatrix4fv(m_qvm, 1, true, m_transformer.get(), 0);
        m_gl.glUniformMatrix4fv(m_qpm, 1, true, m_projection.get(), 0);
        m_gl.glDrawElements(GL.GL_TRIANGLES, 6, GL2.GL_UNSIGNED_BYTE, rectIndexes);
        m_qprog.useProgram(m_gl, false);

    }

    @Override
    public void setClearColor(Vec4f color) {
        m_gl.glClearColor(color.x(), color.y(), color.z(), color.w());
    }

    @Override
    public void setClearDepth(double val) {
        m_gl.glClearDepth(val);
    }

    @Override
    public void setClearStencil(int val) {
        m_gl.glClearStencil(val);
    }

    @Override
    public void clear(int mask) {
        m_gl.glClear(mask);
    }

    protected void syncronizeState(boolean local, boolean newState, int cap) {
        if (newState != local) {
            if (newState) {
                m_gl.glEnable(cap);
                local = true;
            } else {
                m_gl.glDisable(cap);
                local = false;
            }
        }
    }

    @Override
    public void setDepthMode(int mode) {

        switch (mode) {
        case MODE_OFF:
            syncronizeState(m_depthTest, false, GL.GL_DEPTH_TEST);
            break;

        case DEPTH_MODE_NORMAL:
            syncronizeState(m_depthTest, true, GL.GL_DEPTH_TEST);
            m_gl.glDepthFunc(GL.GL_LESS);
            break;

        case DEPTH_MODE_OVER:
            syncronizeState(m_depthTest, true, GL.GL_DEPTH_TEST);
            m_gl.glDepthFunc(GL.GL_LEQUAL);
            break;
        }
    }

    public static int ww;
    public static int hh;

    @Override
    public void setOrtho(int w, int h) {
        ww = w;
        hh = h;
        float[] m = { 2.0f / (float) w, 0.0f, 0.0f, -1.0f, 0.0f, -2.0f / (float) h, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f };
        m_projection.set(m);
    }

    public void setStencilRef(int ref) {
        m_stencilRef = ref;
    }

    @Override
    public void setStencilMode(int mode) {

        switch (mode) {
        case MODE_OFF:
            syncronizeState(m_stencilTest, false, GL.GL_STENCIL_TEST);
            break;

        case STENCIL_MODE_MASK:
            syncronizeState(m_stencilTest, true, GL.GL_STENCIL_TEST);
            m_gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);
            m_gl.glStencilFunc(GL.GL_NOTEQUAL, m_stencilRef, 0xff);
            break;

        case STENCIL_MODE_WRITE:
            syncronizeState(m_stencilTest, true, GL.GL_STENCIL_TEST);
            m_gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_REPLACE);
            m_gl.glStencilFunc(GL.GL_ALWAYS, m_stencilRef, 0xff);
            break;

        case STENCIL_MODE_ONCE:
            syncronizeState(m_stencilTest, true, GL.GL_STENCIL_TEST);
            m_gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_REPLACE);
            m_gl.glStencilFunc(GL.GL_NOTEQUAL, m_stencilRef, 0xff);
            break;
        }
    }

    @Override
    public void setBlendMode(int mode) {

        switch (mode) {
        case MODE_OFF:
            syncronizeState(m_blend, false, GL.GL_BLEND);
            break;

        case BLEND_MODE_ADD:
            syncronizeState(m_blend, true, GL.GL_BLEND);
            m_gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
            break;

        case BLEND_MODE_MUL:
            syncronizeState(m_blend, true, GL.GL_BLEND);
            m_gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ZERO);
            break;

        case BLEND_MODE_TRANS:
            syncronizeState(m_blend, true, GL.GL_BLEND);
            m_gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
            break;
        }
    }

    static int[] tmp = new int[1];

    protected int allocateBuffer(long length, int type) {
        m_gl.glGenBuffers(1, tmp, 0);
        int idx = tmp[0];
        m_gl.glBindBuffer(GL.GL_ARRAY_BUFFER, idx);
        m_gl.glBufferData(GL.GL_ARRAY_BUFFER, length, null, type);
        return idx;
    }

    @Override
    public IDrawBuffer createBuffer() {
        return new PlanarComponentBuffer(this);
    }

    @Override
    public IDrawBuffer createQuadsBuffer() {
        return new QuadsBuffer(this);
    }

    @Override
    public ITransformer3D trans() {
        return m_transformer;
    }

    @Override
    public ITransformer3D proj() {
        return m_projection;
    }

}
