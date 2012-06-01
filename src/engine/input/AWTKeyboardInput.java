package engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.awt.GLCanvas;

public class AWTKeyboardInput {

    public static interface AtlasKeyListener {
        public void keyTyped(char c);

        public void keyReleased(KeyEvent e);

        public void keyPressed(KeyEvent e);
    }

    private AtlasKeyListener m_keyListener;

    public void addControls(GLCanvas canvas) {

        canvas.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (m_keyListener != null) m_keyListener.keyTyped(e.getKeyChar());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (m_keyListener != null) m_keyListener.keyReleased(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (m_keyListener != null) m_keyListener.keyPressed(e);
            }

        });
    }

    public void setKeyListener(AtlasKeyListener kl) {
        m_keyListener = kl;
    }
}
