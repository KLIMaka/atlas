package engine;

import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import engine.drawer.GL2Impl;
import engine.drawer.IRender;
import engine.input.AWTKeyboardInput;
import engine.input.AWTMouseInput;
import engine.input.MouseInput;

public class AtlasEngine implements GLEventListener {

    public MouseInput       mouse;
    public IRender          drawer;
    public AWTKeyboardInput keyboard;

    public static GL2ES2    gl;

    public GLCanvas createAWTCanvas() {

        GLProfile glp = GLProfile.getGL2ES2();
        GLCapabilities caps = new GLCapabilities(glp);
        caps.setAlphaBits(8);
        caps.setStencilBits(8);
        GLCanvas canvas = new GLCanvas(caps);

        AWTMouseInput m = new AWTMouseInput();
        m.addControls(canvas);
        mouse = m;

        AWTKeyboardInput k = new AWTKeyboardInput();
        k.addControls(canvas);
        keyboard = k;

        canvas.addGLEventListener(this);

        return canvas;
    }

    public void draw(IRender drawer) {}

    public void init(IRender drawer) {}

    @Override
    public void init(GLAutoDrawable drawable) {
        drawer = new GL2Impl();
        gl = drawable.getGL().getGL2ES2();
        drawer.setGL(gl);
        drawer.gl().setSwapInterval(0);
        init(drawer);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void display(GLAutoDrawable drawable) {
        draw(drawer);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        drawer.setOrtho(width, height);
    }

}
