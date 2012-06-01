package main;

import java.awt.Font;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLES2;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import types.DrawableElement;
import types.ElementsFactory;
import types.Engine;
import types.IPass;
import types.OutlineRender;
import types.PanelElement;
import types.ScriptFactory;
import types.Sprite;
import types.SpriteFactory;
import types.Stage;
import types.TextureManager;
import types.UIFactory;
import types.renders.PanelRender;
import types.renders.PanelSelectRender;
import types.renders.SelectRender;
import types.renders.SpriteRender;

import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;

public class SimpleScene implements GLEventListener {

    private GLU                    glu            = new GLU();
    private int                    m_h;
    private int                    m_w;

    private SpriteRender           m_spriteRender;
    private SelectRender           m_selectRender;
    private OutlineRender          m_outlineRender;
    private PanelRender            m_panelRender;
    private PanelSelectRender      m_panelSelectRender;

    private DrawableElement        m_selected     = null;
    private DrawableElement        m_selectedPrev = null;
    private DrawableElement        m_down         = null;

    private ArrayList<Sprite>      m_sprites      = new ArrayList<Sprite>();
    private TextureManager         m_texMgr;
    private SpriteFactory          m_spriteFactory;
    private UIFactory              m_uiFactory;
    private DrawableElement        m_dragged;
    private float[]                m_dragOff      = { 0.0f, 0.0f, 0.0f };
    private Stage                  m_selectStage;
    private Stage                  m_lightStage;
    private Stage                  m_outlineStage;
    private Stage                  m_uiStage;
    private PanelElement           m_panel;
    private PanelElement           m_panel1;

    private Engine                 m_engine       = new Engine();
    private ScriptFactory          m_scriptFactory;

    public static String           font1          = "Fallouty";
    public static String           font2          = "Gothic 821 Condensed BT";

    private static AWTMouseControl m2d            = new AWTMouseControl();
    private static Frame           frame;

    public static void main(String[] args) {

        GLProfile glp = GLProfile.getGL2ES2();
        GLCapabilities caps = new GLCapabilities(glp);
        caps.setAlphaBits(8);
        caps.setStencilBits(8);
        GLCanvas canvas = new GLCanvas(caps);
        m2d.addControls(canvas);

        frame = new Frame("Atlas Engine");
        frame.setSize(800, 600);
        frame.add(canvas);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        canvas.addGLEventListener(new SimpleScene());

        AnimatorBase animator = new FPSAnimator(canvas, 60);
        animator.add(canvas);
        animator.start();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        update();
        m_selectStage.exec();
        updateSelection();
        m_lightStage.exec();
        // if (m_selected != null) {
        // m_outlineStage.exec();
        // }
        m_uiStage.exec();
    }

    private void updateSelection() {

        if (m_dragged != null) {
            if (m2d.buttons[0] == 0) {
                m_dragged = null;
            } else {
                m_selected = m_dragged;
            }
        }

        if (m_dragged == null && m_selected != null) {
            if (m2d.buttons[0] == 1) {
                m_dragged = m_selected;
                m_dragOff[0] = m_selected.worldPos()[0] - m2d.x;
                m_dragOff[1] = m_selected.worldPos()[1] - m2d.y;
                m_dragOff[2] = m_selected.worldPos()[2] - m2d.z;
            }
        }

        if (m_selected != m_selectedPrev) {
            if (m_selectedPrev != null) {
                m_selectedPrev.post("mouseExit");
            }
            if (m_selected != null) {
                m_selected.post("mouseEnter");
            }
        } else {
            if (m_selected != null) {
                m_selected.post("mouseMove");
            }
        }

        if (m_selected != null) {
            if (m2d.released) {
                m_selected.post("mouseUp");
            }
            if (m2d.pressed) {
                m_down = m_selected;
                m_selected.post("mouseDown");
            }
            if (m2d.released && m_selected == m_down) {
                m_selected.post("mouseClick");
            }
        }

        m2d.reset();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void init(GLAutoDrawable drawable) {

        GLES2 gl = drawable.getGL().getGLES2();
        m_texMgr = new TextureManager();
        m_spriteFactory = new SpriteFactory(m_texMgr);
        m_uiFactory = new UIFactory(m_texMgr);
        m_spriteRender = new SpriteRender(gl);
        m_outlineRender = new OutlineRender(gl);
        m_selectRender = new SelectRender(gl);
        m_panelRender = new PanelRender(gl);
        m_panelSelectRender = new PanelSelectRender(gl);
        m_scriptFactory = new ScriptFactory(m_engine);

        m_engine.mouse = m2d;
        m_engine.frame = frame;
        m_engine.ui = m_uiFactory;
        m_engine.scripts = m_scriptFactory;

        Sprite sphere = m_spriteFactory.create("data/sphere/sphere", 55, 32, 50, -1.0f, -1.0f);
        sphere.setMulColor(0.5f, 0.1f, 0.1f, 1.0f);
        Sprite barrel = m_spriteFactory.create("data/barrel/barrel", 55, 32, 50, -1.0f, -1.0f);
        barrel.setMulColor(0.1f, 0.5f, 0.1f, 1.0f);
        Sprite cube = m_spriteFactory.create("data/fcube/fcube", 55, 32, 50, -1.0f, -1.0f);
        cube.setMulColor(0.1f, 0.1f, 0.5f, 1.0f);
        Sprite bot = m_spriteFactory.create("data/bot/bot", 175, 78, 178, -1.0f, -1.0f);
        bot.setMulColor(0.6f, 0.6f, 0.2f, 1.0f);
        Sprite car = m_spriteFactory.create("data/car/car", 95, 100, 110, -1.0f, -1.0f);
        car.setMulColor(0.6f, 0.2f, 0.6f, 1.0f);
        Sprite ogr = m_spriteFactory.create("data/ogr/ogr", 160, 100, 170, -1.0f, -1.0f);

        m_panel = m_uiFactory.createPanel("data/panel/panel.png", 0.1f);
        m_panel.setPos(200, 200);
        m_panel.setSize(200, 300);
        m_panel.setScript(m_scriptFactory.create("scripts/foo.lua"));

        Sprite[] spr = { cube, sphere, barrel, bot, car, ogr };
        for (int i = 0; i < 600; i++) {
            m_sprites.add(m_spriteFactory.clone(spr[2]));
            m_sprites.get(i).setPos((float) Math.random() * 800.0f, (float) Math.random() * 600.0f, 0.0f);
            m_sprites.get(i).scale(1.0f);
        }

        gl.glEnable(GL2.GL_TEXTURE_2D);

        final TextRenderer tr = new TextRenderer(new Font("Tahoma", Font.PLAIN, 10), true);

        m_selectStage = new Stage(gl, new Stage.ISetup() {
            public void setup(GLES2 gl) {
                gl.glLoadIdentity();
                gl.glClearDepthf(1.0f);
                gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                gl.glClear(GLES2.GL_COLOR_BUFFER_BIT | GLES2.GL_DEPTH_BUFFER_BIT);
                gl.glEnable(GLES2.GL_DEPTH_TEST);
                gl.glDepthFunc(GLES2.GL_LESS);

            }
        }, new IPass[] { new IPass() {

            public void exec(GLES2 gl) {
                m_selectRender.begin();
                for (Sprite spr : m_sprites) {
                    m_selectRender.draw(spr);
                }
                m_selectRender.end();

                gl.glDisable(GLES2.GL_DEPTH_TEST);

                m_panelSelectRender.begin();
                m_panelSelectRender.draw(m_panel);
                m_panelSelectRender.end();

                int id = m_selectRender.getId(m2d.x, m_h - m2d.y);
                DrawableElement elem = ElementsFactory.get(id);

                m_selectedPrev = m_selected;
                m_selected = elem;
            }
        } }, new Stage.ISetup() {
            public void setup(GLES2 gl) {}
        });

        m_lightStage = new Stage(gl, new Stage.ISetup() {
            public void setup(GLES2 gl) {
                gl.glClearStencil(0);
                gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                gl.glClear(GLES2.GL_COLOR_BUFFER_BIT | GLES2.GL_STENCIL_BUFFER_BIT);
                gl.glEnable(GLES2.GL_DEPTH_TEST);
                gl.glDepthFunc(GLES2.GL_LEQUAL);
                gl.glEnable(GLES2.GL_BLEND);
                gl.glBlendFunc(GLES2.GL_ONE, GL2.GL_ONE);
                gl.glEnable(GLES2.GL_STENCIL_TEST);
            }
        }, new IPass[] { new IPass() {
            public void exec(GLES2 gl) {
                gl.glStencilOp(GLES2.GL_KEEP, GLES2.GL_KEEP, GLES2.GL_REPLACE);
                gl.glStencilFunc(GLES2.GL_NOTEQUAL, 1, 0xff);
                m_spriteRender.begin();
                m_spriteRender.setLightPos(m2d.x, m_h - m2d.y, m2d.z);

                for (Sprite spr : m_sprites) {
                    m_spriteRender.draw(spr);
                }

                gl.glStencilOp(GLES2.GL_KEEP, GLES2.GL_KEEP, GLES2.GL_REPLACE);
                gl.glStencilFunc(GLES2.GL_NOTEQUAL, 2, 0xff);
                m_spriteRender.setLightPos(m_w - m2d.x, m2d.y, m2d.z);
                for (Sprite spr : m_sprites) {
                    m_spriteRender.draw(spr);
                }

                m_spriteRender.end();
            }
        } }, new Stage.ISetup() {
            public void setup(GLES2 gl) {
                gl.glDisable(GLES2.GL_BLEND);
                gl.glDisable(GLES2.GL_STENCIL_TEST);
            }
        });

        m_outlineStage = new Stage(gl, new Stage.ISetup() {
            public void setup(GLES2 gl) {
                gl.glDisable(GLES2.GL_DEPTH_TEST);
                gl.glEnable(GLES2.GL_BLEND);
                gl.glBlendFunc(GLES2.GL_ONE, GLES2.GL_ZERO);
                gl.glPushMatrix();
                gl.glLoadIdentity();
            }
        }, new IPass[] { new IPass() {

            public void exec(GLES2 gl) {
                m_outlineRender.begin();
                m_outlineRender.draw(m_selected);
                m_outlineRender.end();

                tr.begin3DRendering();
                gl.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
                gl.glTranslatef(0.0f, -m_h, 0.0f);
                tr.draw3D("Value = " + String.valueOf(m_selected.id()), 0, 550, 0, 0.25f);
                tr.end3DRendering();
            }
        } }, new Stage.ISetup() {
            public void setup(GLES2 gl) {
                gl.glDisable(GLES2.GL_BLEND);
                gl.glPopMatrix();
            }
        });

        m_uiStage = new Stage(gl, new Stage.ISetup() {
            public void setup(GLES2 gl) {
                gl.glDisable(GLES2.GL_DEPTH_TEST);
                gl.glEnable(GLES2.GL_BLEND);
                gl.glBlendFunc(GLES2.GL_SRC_ALPHA, GLES2.GL_ONE_MINUS_SRC_ALPHA);
            }
        }, new IPass[] { new IPass() {

            public void exec(GLES2 gl) {

                m_panelRender.begin();
                m_panelRender.draw(m_panel);
                m_panelRender.end();
            }
        } }, new Stage.ISetup() {
            public void setup(GLES2 gl) {
                gl.glDisable(GLES2.GL_BLEND);
            }
        });

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {

        m_w = w;
        m_h = h;
        m_engine.setScreenSize(w, h);

        GLES2 gl = drawable.getGL().getGLES2();
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0, w, h, 0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private void update() {

        if (m_dragged != null && m_dragged instanceof Sprite) {
            Sprite spr = (Sprite) m_dragged;
            spr.setPos(m2d.x + m_dragOff[0], m2d.y + m_dragOff[1], m2d.z + m_dragOff[2]);
        }
    }

}
