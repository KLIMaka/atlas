package engine.drawer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.media.opengl.GL;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.GLBuffers;

import engine.buffers.IDrawBuffer;
import engine.buffers.bufferConfig;
import engine.drawer.accsessors.AccessorQuadFloat;
import engine.drawer.accsessors.AccessorQuadUByte;
import engine.drawer.accsessors.IBufferAccsessor;

public class QuadsBuffer implements IDrawBuffer {

    private GL2Impl                 m_render;

    private int                     m_length;
    private ArrayList<bufferConfig> m_cfgs       = new ArrayList<bufferConfig>();
    private IBufferAccsessor[][]    m_accessors  = new IBufferAccsessor[4][];
    private int                     m_idx;
    private ByteBuffer              m_buffer;
    private boolean                 m_needUpdate = true;

    public QuadsBuffer(GL2Impl render) {
        m_render = render;
    }

    public void addComponent(int size, int type, int count, boolean norm) {
        m_cfgs.add(new bufferConfig(size, type, norm, 0, m_length, count * 4));
        m_length += 4 * count * size * GLBuffers.sizeOfGLType(type);
    }

    @Override
    public List<bufferConfig> getConfig() {
        return Collections.unmodifiableList(m_cfgs);
    }

    @Override
    public void allocate(int usage) {
        m_idx = m_render.allocateBuffer(m_length, usage);
        m_buffer = Buffers.newDirectByteBuffer(m_length);
    }

    protected IBufferAccsessor genAccessor(int comp, int off) {

        IBufferAccsessor[] accs = m_accessors[off];
        if (accs == null) {
            accs = new IBufferAccsessor[m_cfgs.size()];
            m_accessors[off] = accs;
        }

        IBufferAccsessor acc = accs[comp];
        if (acc == null) {
            bufferConfig cfg = m_cfgs.get(comp);
            int size = GLBuffers.sizeOfGLType(cfg.type) * cfg.size * cfg.count;
            ByteBuffer sliced = Buffers.slice(m_buffer, cfg.offset, size);

            switch (cfg.type) {
            case GL.GL_FLOAT:
                acc = new AccessorQuadFloat(sliced.asFloatBuffer(), cfg.size, off);
                break;

            case GL.GL_UNSIGNED_BYTE:
                acc = new AccessorQuadUByte(sliced, cfg.size, off);
                break;

            }

            m_accessors[off][comp] = acc;
        }

        return acc;
    }

    @Override
    public int size() {
        return m_length;
    }

    @Override
    public IBufferAccsessor getAccsessor(int comp, int off) {
        return genAccessor(comp, off);
    }

    @Override
    public IBufferAccsessor getAccsessor(int comp) {
        return getAccsessor(comp, 0);
    }

    @Override
    public void bind() {
        m_render.bindBuffer(m_idx);
    }

    @Override
    public void update() {

        bind();
        m_render.updateBuffer(0, m_length, m_buffer);
    }

}
