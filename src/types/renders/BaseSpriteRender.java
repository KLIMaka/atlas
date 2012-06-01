package types.renders;

import java.nio.FloatBuffer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLES2;

import main.Attribs;
import types.DrawableElement;

import com.jogamp.opengl.util.GLBuffers;

public abstract class BaseSpriteRender extends Render {

    private int[] m_buffers = { 0 };

    public BaseSpriteRender(GLES2 gl, String shader, int[] tca, String[] txn, int[] txi) {

        super(gl, shader, tca, txn, txi);

        gl().glGenBuffers(1, m_buffers, 0);
        FloatBuffer posbuf = GLBuffers.newDirectFloatBuffer(new float[] { 0, 0, 1, 0, 1, 1, 0, 1 });
        gl().glBindBuffer(GL2.GL_ARRAY_BUFFER, m_buffers[0]);
        gl().glBufferData(GL2.GL_ARRAY_BUFFER, 2 * 4 * GLBuffers.SIZEOF_FLOAT, posbuf, GL2.GL_STATIC_DRAW);
    }

    @Override
    public void begin() {

        gl().glBindBuffer(GL2.GL_ARRAY_BUFFER, m_buffers[0]);
        gl().glVertexAttribPointer(Attribs.vertex, 2, GLES2.GL_FLOAT, false, 0, 0);
        gl().glVertexAttribPointer(Attribs.attrib1, 2, GLES2.GL_FLOAT, false, 0, 0);

        gl().glEnableVertexAttribArray(Attribs.vertex);
        gl().glEnableVertexAttribArray(Attribs.attrib1);

        // gl().glVertexPointer(2, GL2.GL_FLOAT, 0, 0);
        // gl().glTexCoordPointer(2, GL2.GL_FLOAT, 0, 0);
        //
        // gl().glEnableClientState(GL2.GL_VERTEX_ARRAY);
        // gl().glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

        super.begin();
    }

    @Override
    public void end() {

        super.end();

        gl().glDisableVertexAttribArray(Attribs.vertex);
        gl().glDisableVertexAttribArray(Attribs.attrib1);

        // gl().glDisableClientState(GL2.GL_VERTEX_ARRAY);
        // gl().glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
    }

    public void draw(DrawableElement elem) {

        prepare(elem);
        gl().glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);
    }

}
