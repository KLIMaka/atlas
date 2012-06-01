package types;

import javax.media.opengl.GLES2;

public class Stage {

    public interface ISetup {
        void setup(GLES2 gl);
    }

    private GLES2   m_gl;
    private ISetup  m_pre;
    private ISetup  m_post;
    private IPass[] m_passes;

    public Stage(GLES2 gl, ISetup pre, IPass[] passes, ISetup post) {
        m_gl = gl;
        m_pre = pre;
        m_post = post;
        m_passes = passes;
    }

    public void exec() {
        m_pre.setup(m_gl);
        for (IPass pass : m_passes) {
            pass.exec(m_gl);
        }
        m_post.setup(m_gl);
    }

}
