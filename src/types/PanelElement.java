package types;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLES2;

import main.Attribs;

import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.Texture;

public class PanelElement extends UIElement {

    private float                   m_delta;
    private boolean                 m_inited = false;
    private int[]                   m_buffer = { 0 };
    private GLES2                   m_gl;
    private float                   m_w;
    private float                   m_h;
    private float                   m_minw;
    private float                   m_minh;
    private float                   m_bord;

    private PanelElement            m_parent;
    private ArrayList<PanelElement> m_childs = new ArrayList<PanelElement>(0);

    protected PanelElement(Texture tex, float d) {
        super(tex, 0, 0);
        m_delta = d;
        m_w = colorTex().getImageWidth();
        m_h = colorTex().getImageHeight();
        m_minw = m_w * d * 2.0f;
        m_minh = m_h * d * 2.0f;
        m_bord = m_w * d;
    }

    public void setSize(float w, float h) {
        m_w = w < m_minw ? m_minw : w;
        m_h = h < m_minh ? m_minh : h;
        if (m_inited) {
            resize();
        }
        post("onResize");
    }

    public float width() {
        return m_w;
    }

    public float height() {
        return m_h;
    }

    public PanelElement parent() {
        return m_parent;
    }

    public ArrayList<PanelElement> childs() {
        return m_childs;
    }

    public void addChild(PanelElement pe) {
        m_childs.add(pe);
        pe.setParent(this);
    }

    public void setParent(PanelElement pe) {
        m_parent = pe;
    }

    private void resize() {

        final int POINT_COUNT = 16;
        final int TC_SIZE = POINT_COUNT * GLBuffers.SIZEOF_FLOAT * 2;
        final int WPOS_SIZE = POINT_COUNT * GLBuffers.SIZEOF_FLOAT * 2;

        FloatBuffer tcs = GLBuffers.newDirectFloatBuffer(POINT_COUNT * 2);
        float[] fracs = new float[] { 0.0f, m_delta, 1.0f - m_delta, 1.0f };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tcs.put(fracs[j]);
                tcs.put(fracs[i]);
            }
        }
        tcs.rewind();

        FloatBuffer wposs = GLBuffers.newDirectFloatBuffer(POINT_COUNT * 2);
        fracs = new float[] { 0.0f, m_bord, m_w - m_bord, m_w };
        float[] fracs1 = new float[] { 0.0f, m_bord, m_h - m_bord, m_h };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                wposs.put(fracs[j]);
                wposs.put(fracs1[i]);
            }
        }
        wposs.rewind();

        m_gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, m_buffer[0]);
        m_gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, TC_SIZE, wposs);
        m_gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, TC_SIZE, WPOS_SIZE, tcs);
    }

    private void initBuffer(GLES2 gl) {

        m_gl = gl;
        m_gl.glGenBuffers(1, m_buffer, 0);

        m_gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, m_buffer[0]);
        int size = GLBuffers.SIZEOF_FLOAT * 2 * 2 * 16;
        m_gl.glBufferData(GL2.GL_ARRAY_BUFFER, size, null, GL2.GL_STREAM_DRAW);
    }

    public void bindBuffers(GLES2 gl) {

        if (!m_inited) {
            initBuffer(gl);
            resize();
            m_inited = true;
        }

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, m_buffer[0]);
        gl.glVertexAttribPointer(Attribs.vertex, 2, GLES2.GL_FLOAT, false, 0, 0);
        gl.glVertexAttribPointer(Attribs.attrib1, 2, GLES2.GL_FLOAT, false, 0, 16 * 2 * 4);

        // gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, 16 * 2 * 4);
    }

    public int getMode() {
        return GL2.GL_QUADS;
    }

    public int getCount() {
        return 36;
    }

    public int getType() {
        return GL2.GL_UNSIGNED_BYTE;
    }

    public Buffer getIndices() {
        final ByteBuffer indices = GLBuffers.newDirectByteBuffer(new byte[] { 0, 1, 5, 4, 1, 2, 6, 5, 2, 3, 7, 6, 4, 5,
                9, 8, 5, 6, 10, 9, 6, 7, 11, 10, 8, 9, 13, 12, 9, 10, 14, 13, 10, 11, 15, 14 });
        return indices;
    }
}
