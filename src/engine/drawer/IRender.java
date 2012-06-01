package engine.drawer;

import engine.buffers.IDrawBuffer;
import engine.transformer.ITransformer3D;
import gleem.linalg.Vec4f;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;

import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.texture.Texture;

public interface IRender {

    public static final int COLOR              = GL.GL_COLOR_BUFFER_BIT;
    public static final int DEPTH              = GL.GL_DEPTH_BUFFER_BIT;
    public static final int STENCIL            = GL.GL_STENCIL_BUFFER_BIT;

    public static final int MODE_OFF           = -1;

    public static final int DEPTH_MODE_NORMAL  = 1;
    public static final int DEPTH_MODE_OVER    = 2;

    public static final int STENCIL_MODE_MASK  = 1;
    public static final int STENCIL_MODE_WRITE = 2;
    public static final int STENCIL_MODE_ONCE  = 3;

    public static final int BLEND_MODE_ADD     = 1;
    public static final int BLEND_MODE_MUL     = 2;
    public static final int BLEND_MODE_TRANS   = 3;

    public void setGL(GL2ES2 gl);

    public GL2ES2 gl();

    public void setClearColor(Vec4f color);

    public void setClearDepth(double val);

    public void setClearStencil(int val);

    public void clear(int mask);

    public void setDepthMode(int mode);

    public void setStencilMode(int mode);

    public void setBlendMode(int mode);

    public static final int BUFFER_STATIC  = GL.GL_STATIC_DRAW;
    public static final int BUFFER_DYNAMIC = GL.GL_DYNAMIC_DRAW;
    public static final int BUFFER_STREAM  = GL2.GL_STREAM_DRAW;

    public IDrawBuffer createBuffer();

    public IDrawBuffer createQuadsBuffer();

    public void setOrtho(int w, int h);

    public void drawSprites(IDrawBuffer buf, int idxs[], int comps[]);

    public ITransformer3D trans();

    public ITransformer3D proj();

    public void drawRect(float left, float top, float right, float bottom);

    public void drawQuads(IDrawBuffer buf, int[] idxs, int off, int count, int[] comps);

    public ShaderProgram loadShader(String name);

    public void bindShader(ShaderProgram shader);

    public void unbindShader(ShaderProgram shader);

    public void bindTexture(Texture tex);

    public void unbindTexture(Texture tex);

}
