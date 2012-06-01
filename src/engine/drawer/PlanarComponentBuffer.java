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
import engine.drawer.accsessors.AccessorFloat;
import engine.drawer.accsessors.AccessorUByte;
import engine.drawer.accsessors.IBufferAccsessor;

public class PlanarComponentBuffer implements IDrawBuffer {

    private GL2Impl                     m_render;

    private ArrayList<bufferConfig>     m_cfgs      = new ArrayList<bufferConfig>();
    private ArrayList<IBufferAccsessor> m_accessors = new ArrayList<IBufferAccsessor>();
    private int                         m_length    = 0;
    private int                         m_idx;
    private boolean                     m_neddUpdate;

    private ByteBuffer                  m_buffer;

    protected PlanarComponentBuffer(GL2Impl render) {
        m_render = render;

    }

    public void addComponent(int size, int type, int count, boolean norm) {
        m_cfgs.add(new bufferConfig(size, type, norm, 0, m_length, count));
        m_length += count * size * GLBuffers.sizeOfGLType(type);
    }

    private void genAccsessors() {

        for (bufferConfig cfg : m_cfgs) {

            int size = GLBuffers.sizeOfGLType(cfg.type) * cfg.size * cfg.count;
            ByteBuffer sliced = Buffers.slice(m_buffer, cfg.offset, size);

            switch (cfg.type) {
            case GL.GL_FLOAT:
                m_accessors.add(new AccessorFloat(sliced.asFloatBuffer()));
                break;

            case GL.GL_UNSIGNED_BYTE:
                m_accessors.add(new AccessorUByte(sliced));
                break;

            }

        }
    }

    public List<bufferConfig> getConfig() {
        return Collections.unmodifiableList(m_cfgs);
    }

    public IBufferAccsessor getAccsessor(int comp, int off) {
        return m_accessors.get(comp);
    }

    @Override
    public IBufferAccsessor getAccsessor(int comp) {
        return m_accessors.get(comp);
    }

    @Override
    public void allocate(int usage) {
        m_idx = m_render.allocateBuffer(m_length, usage);
        m_buffer = Buffers.newDirectByteBuffer(m_length);
        genAccsessors();
    }

    @Override
    public int size() {
        return m_length;
    }

    public void bind() {
        m_render.bindBuffer(m_idx);
    }

    public void update() {
        if (!m_neddUpdate) return;

        bind();
        m_render.updateBuffer(0, m_length, m_buffer);
    }
}
