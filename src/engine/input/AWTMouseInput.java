package engine.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.media.opengl.awt.GLCanvas;

public class AWTMouseInput implements MouseInput {

    public int     x;
    public int     y;
    public int     z;

    public int[]   buttons  = { 0, 0, 0 };

    public boolean pressed  = false;
    public boolean released = false;

    public AWTMouseInput() {}

    public void reset() {
        pressed = false;
        released = false;
    }

    public void addControls(GLCanvas canvas) {

        canvas.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                z -= e.getWheelRotation();
            }
        });

        canvas.addMouseMotionListener(new MouseMotionListener() {

            public void mouseMoved(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });

        canvas.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {

                switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    buttons[0] = 0;
                    break;
                case MouseEvent.BUTTON2:
                    buttons[1] = 0;
                    break;
                case MouseEvent.BUTTON3:
                    buttons[2] = 0;
                    break;
                }

                released = true;
            }

            @Override
            public void mousePressed(MouseEvent e) {

                switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    buttons[0] = 1;
                    break;
                case MouseEvent.BUTTON2:
                    buttons[1] = 1;
                    break;
                case MouseEvent.BUTTON3:
                    buttons[2] = 1;
                    break;
                }

                pressed = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseClicked(MouseEvent e) {}
        });
    }

    @Override
    public boolean isReleaseAction() {
        return released;
    }

    @Override
    public boolean isPressAction() {
        return pressed;
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    @Override
    public int z() {
        return z;
    }

    @Override
    public int button(int button) {
        return buttons[button];
    }
}
