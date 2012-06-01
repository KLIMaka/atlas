package engine.elements;

import engine.monads.IMonadDrawable;
import engine.monads.IMonadID;
import engine.monads.IMonadPos;
import engine.monads.IMonadRect;

public class Sprite implements IElement, IMonadID, IMonadPos, IMonadRect, IMonadDrawable {

    private SpriteFactory m_parent;
    private int           m_bufIdx;

    private float         m_w, m_h;
    private float         m_ox, m_oy;
    private float         m_x, m_y;
    private float         m_rot;
    private int           m_idx;
    private boolean       m_isShown;

    protected Sprite(SpriteFactory fact, int idx) {
        m_parent = fact;
        m_bufIdx = idx;
        int x = (int) (Math.random() * 256.0);
        m_parent.m_aColor.put4ub(m_bufIdx, x, x, x, x);
    }

    @Override
    public String toString() {
        return String.format("sprite 0x%1$08X", m_idx);
    }

    @Override
    public void setRect(float w, float h) {
        if (w != m_w || h != m_h) {
            m_w = w;
            m_h = h;
            m_parent.m_aSize.put2f(m_bufIdx, m_w, m_h);
        }
    }

    @Override
    public void resize(float dw, float dh) {
        if (dw != 0.0f && dh != 0.0f) {
            m_w += dw;
            m_h += dh;
            m_parent.m_aSize.put2f(m_bufIdx, m_w, m_h);
        }
    }

    @Override
    public void setOrigin(float x, float y) {
        if (x != m_ox || y != m_oy) {
            m_ox = x;
            m_oy = y;
            m_parent.m_aOrigin.put2f(m_bufIdx, m_ox, m_oy);
        }
    }

    @Override
    public void setRotation(float angle) {
        if (angle != m_rot) {
            m_rot = angle;
            m_parent.m_aRot.put1f(m_bufIdx, m_rot);
        }
    }

    @Override
    public float height() {
        return m_h;
    }

    @Override
    public float width() {
        return m_w;
    }

    @Override
    public float xOrigin() {
        return m_ox;
    }

    @Override
    public float yOrigin() {
        return m_oy;
    }

    @Override
    public float angle() {
        return m_rot;
    }

    @Override
    public void setPos(float x, float y) {
        if (m_x != x || m_y != y) {
            m_x = x;
            m_y = y;
            m_parent.m_aPos.put2f(m_bufIdx, m_x, m_y);
        }
    }

    @Override
    public void move(float dx, float dy) {
        if (dx != 0.0f && dy != 0.0f) {
            m_x += dx;
            m_y += dy;
            m_parent.m_aPos.put2f(m_bufIdx, m_x, m_x);
        }
    }

    @Override
    public float x() {
        return m_x;
    }

    @Override
    public float y() {
        return m_y;
    }

    @Override
    public int getID() {
        return m_idx;
    }

    @Override
    public void setID(int id) {
        m_idx = id;
    }

    @Override
    public void hide() {
        m_isShown = false;
        m_parent.setShow(m_bufIdx, m_isShown);
    }

    @Override
    public void show() {
        m_isShown = true;
        m_parent.setShow(m_bufIdx, m_isShown);
    }

    @Override
    public boolean isShown() {
        return m_isShown;
    }

}
