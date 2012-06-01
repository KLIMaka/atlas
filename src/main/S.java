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
import engine.elements.Sprite;
import engine.elements.SpriteFactory;
import engine.input.AWTKeyboardInput.AtlasKeyListener;
import engine.text.TextRender;
import gleem.linalg.Vec4f;

public class S extends AtlasEngine {

    private static GLCanvas m_canvas;

    static SpriteFactory    sprites;
    static TextRender       text;
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
        int x = mouse.x();
        int y = mouse.y();

        draw.trans().reset();
        draw.trans().translate(10, 0);

        // text.setRow(mouse.z() * 5);
        text.draw();
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
        sprites = new SpriteFactory(drawer);
        for (int i = 0; i < count; i++) {
            float x = (float) (Math.random() * 2.0f - 1.0f);
            float y = (float) (Math.random() * 2.0f - 1.0f);
            Sprite spr = sprites.create(10, 10);
            spr.show();
            spr.setPos(x * 600, y * 600);
        }

        text = new TextRender("fonts/curier.fnt", drawer);
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
            public void keyPRessed(KeyEvent e) {
                text.key(e);
            }
        });
    }
}
