package types;

import gleem.linalg.Vec3f;

import java.util.ArrayList;

import javax.media.opengl.GLAutoDrawable;

public abstract class Node
{

    private Node                m_parent;
    private Drawer              m_drawer;
    private Drawer              m_preDrawer;
    private Updater             m_updater;
    private int                 m_id;
    private Vec3f               m_idColor;
    private ArrayList<GeomNode> m_childs = new ArrayList<GeomNode>(0);
    private int                 m_frameStatus;

    public Node(Node parent, int id)
    {
        m_parent = parent;
        m_id = id;

        float r = (m_id & 0xff) / 255.0f;
        float g = ((m_id >> 8) & 0xff) / 255.0f;
        float b = ((m_id >> 16) & 0xff) / 255.0f;
        m_idColor = new Vec3f(r, g, b);
    }

    public void preDraw(GLAutoDrawable drawable)
    {
        if (m_preDrawer != null)
            m_preDrawer.draw(drawable);

        for (GeomNode node : getChilds())
            node.preDraw(drawable);
    }

    public void draw(GLAutoDrawable drawable)
    {
        if (m_drawer != null)
            m_drawer.draw(drawable);

        for (GeomNode node : getChilds())
            node.draw(drawable);
    }

    public void selectDraw(GLAutoDrawable drawable)
    {
        if (m_drawer != null)
            m_drawer.draw(drawable);

        for (GeomNode node : getChilds())
            node.selectDraw(drawable);
    }

    public void update()
    {
        m_frameStatus = 0;

        if (m_updater != null)
            m_updater.update();

        for (GeomNode node : getChilds())
            node.update();
    }

    public void attach(GeomNode child)
    {
        m_childs.add(child);
        child.setParent(this);
    }

    public Node getParent()
    {
        return m_parent;
    }

    public void setParent(Node parent)
    {
        m_parent = parent;
    }

    public Drawer getDrawer()
    {
        return m_drawer;
    }

    public void setDrawer(Drawer drawer)
    {
        m_drawer = drawer;
    }

    public Updater getUpdater()
    {
        return m_updater;
    }

    public void setUpdater(Updater updater)
    {
        m_updater = updater;
    }

    public int getId()
    {
        return m_id;
    }

    public ArrayList<GeomNode> getChilds()
    {
        return m_childs;
    }

    public Drawer getPreDrawer()
    {
        return m_preDrawer;
    }

    public void setPreDrawer(Drawer preDrawer)
    {
        m_preDrawer = preDrawer;
    }

    public int getFrameStatus()
    {
        return m_frameStatus;
    }

    public void setFrameStatus(int status)
    {
        m_frameStatus = status;
    }

    public Vec3f getIdColor()
    {
        return m_idColor;
    }
}
