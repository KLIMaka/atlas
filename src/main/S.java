package main;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;

import engine.AtlasEngine;
import engine.drawer.IRender;
import engine.elements.Panel;
import engine.elements.PanelFactory;
import engine.elements.SpriteFactory;
import engine.input.AWTKeyboardInput.AtlasKeyListener;
import engine.text.MultilineTextRender;
import gleem.linalg.Vec4f;

public class S extends AtlasEngine {

    private static GLCanvas m_canvas;

    static SpriteFactory    sprites;
    static PanelFactory panels;
    static Panel panel;
    static MultilineTextRender       text;
    static int              count = 999;

    static String           input = "";

    public static void main(String[] args) throws IOException {

        S engine = new S();
        Frame frame = new Frame("Atlas Engine");
        frame.setSize(800, 600);
        m_canvas = engine.createAWTCanvas();
        frame.add(m_canvas);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                int fps = 0;
                long last = System.currentTimeMillis();
                for (;;) {
                    m_canvas.display();
                    fps++;
                    if (System.currentTimeMillis() - last > 1000) {
                        last = System.currentTimeMillis();
                        System.out.println(fps);
                        fps = 0;
                    }

                }
            }
        }).start();

    }

    static int   i = 0;
    static float r = 0.0f;

    public void draw(IRender draw) {
        draw.clear(IRender.COLOR);

        draw.trans().reset();
        draw.trans().translate(10, 0);
        text.draw();
        
        panel.setRotation(r+=0.001f);
        panels.draw(panel);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        super.reshape(drawable, x, y, width, height);
        if (text != null) {
            text.setWindow(width - 20, height - 20);
        }
    }

    @Override
    public void init(IRender drawer) {

        drawer.setClearColor(new Vec4f(1, 1, 1, 0.0f));
        
        panels = new PanelFactory(drawer);
        panel = panels.create();
        panel.setRect(100, 200);
        panel.setOrigin(50, 100);
        panel.setPos(200,300);

        text = new MultilineTextRender("fonts/curier.fnt", drawer);
        text.setColor(0, 0, 0, 1);
        text.setThickness(1.0f);
        text.setLineHeight(16.0f);

        keyboard.setKeyListener(new AtlasKeyListener() {

            @Override
            public void keyTyped(char c) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                text.key(e);
            }
        });
    }
}
