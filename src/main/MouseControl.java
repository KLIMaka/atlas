package main;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.media.opengl.GL2;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.awt.GLJPanel;

public class MouseControl
{

    private static float ro = 10, vangle = -45, hangle = 0, xpos = 0, ypos = 0;
    private static boolean lbutton, rbutton;
    private static int     oldX, oldY;

    public int             posx;
    public int             posy;

    MouseControl()
    {
    }

    public void addControls(GLJPanel panel)
    {
        panel.addMouseWheelListener(new MouseWheelListener()
        {
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                ro += (float) (e.getWheelRotation());
            }
        });
        panel.addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e)
            {
            }

            public void mousePressed(MouseEvent e)
            {
                oldX = e.getXOnScreen();
                oldY = e.getYOnScreen();
                if (e.getButton() == 1)
                    lbutton = true;
                if (e.getButton() == 3)
                    rbutton = true;
            }

            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == 1)
                    lbutton = false;
                if (e.getButton() == 3)
                    rbutton = false;
            }

            public void mouseEntered(MouseEvent e)
            {
            }

            public void mouseExited(MouseEvent e)
            {
            }
        });
        panel.addMouseMotionListener(new MouseMotionListener()
        {
            public void mouseDragged(MouseEvent e)
            {
                if (lbutton == true)
                {
                    int curx = e.getXOnScreen(), cury = e.getYOnScreen();
                    double xcoef = Math.sin(Math.toRadians(hangle)), ycoef = Math
                            .cos(Math.toRadians(hangle));

                    xpos += ro
                            * (-(cury - oldY) * xcoef + (curx - oldX) * ycoef)
                            / 400.0f;
                    ypos += ro
                            * ((cury - oldY) * ycoef + (curx - oldX) * xcoef)
                            / 400.0f;

                    oldX = e.getXOnScreen();
                    oldY = e.getYOnScreen();
                }
                if (rbutton == true)
                {
                    hangle += (float) (e.getXOnScreen() - oldX);
                    oldX = e.getXOnScreen();
                    vangle += (float) (e.getYOnScreen() - oldY);
                    oldY = e.getYOnScreen();
                }
            }

            public void mouseMoved(MouseEvent e)
            {
                posx = e.getXOnScreen();
                posy = e.getXOnScreen();
            }
        });
    }

    public void addControls(GLCanvas canvas)
    {
        canvas.addMouseWheelListener(new MouseWheelListener()
        {
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                ro += (float) (e.getWheelRotation());
            }
        });
        canvas.addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e)
            {
            }

            public void mousePressed(MouseEvent e)
            {
                oldX = e.getXOnScreen();
                oldY = e.getYOnScreen();
                if (e.getButton() == 1)
                    lbutton = true;
                if (e.getButton() == 3)
                    rbutton = true;
            }

            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == 1)
                    lbutton = false;
                if (e.getButton() == 3)
                    rbutton = false;
            }

            public void mouseEntered(MouseEvent e)
            {
            }

            public void mouseExited(MouseEvent e)
            {
            }
        });
        canvas.addMouseMotionListener(new MouseMotionListener()
        {
            public void mouseDragged(MouseEvent e)
            {
                if (lbutton == true)
                {
                    int curx = e.getXOnScreen(), cury = e.getYOnScreen();
                    double xcoef = Math.sin(Math.toRadians(hangle)), ycoef = Math
                            .cos(Math.toRadians(hangle));

                    xpos += ro
                            * (-(cury - oldY) * xcoef + (curx - oldX) * ycoef)
                            / 400.0f;
                    ypos += ro
                            * ((cury - oldY) * ycoef + (curx - oldX) * xcoef)
                            / 400.0f;

                    oldX = e.getXOnScreen();
                    oldY = e.getYOnScreen();
                }
                if (rbutton == true)
                {
                    hangle += (float) (e.getXOnScreen() - oldX);
                    oldX = e.getXOnScreen();
                    vangle += (float) (e.getYOnScreen() - oldY);
                    oldY = e.getYOnScreen();
                }
            }

            public void mouseMoved(MouseEvent e)
            {
                posx = e.getX();
                posy = e.getY();
            }
        });
    }

    public void apply(GL2 gl)
    {
        gl.glRotated(90, 1, 0, 0);
        gl.glTranslatef(0.0f, -ro, 0.0f);
        gl.glRotated(vangle, 1, 0, 0);
        gl.glRotated(hangle, 0, 1, 0);
        gl.glTranslatef(xpos, 0.0f, ypos);
    }
}
