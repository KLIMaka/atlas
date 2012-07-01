package engine.elements;

import engine.monads.IMonadDrawable;
import engine.monads.IMonadID;
import engine.monads.IMonadPos;
import engine.monads.IMonadRect;
import engine.transformer.Transformation;
import gleem.linalg.Mat4f;

public class Panel implements IElement, IMonadID, IMonadPos, IMonadRect, IMonadDrawable {

    protected PanelFactory   m_parent;
    protected int            m_bufIdx;

    private float            m_w         = 1.0f, m_h = 1.0f;
    private float            m_x, m_y;
    private float            m_angle;
    private float            m_ox        = 0.5f, m_oy = 0.5f;
    private int              m_idx;

    protected Transformation m_transform = new Transformation();

    protected Panel(PanelFactory parent, int idx) {
        m_parent = parent;
        m_bufIdx = idx;

        setOrigin(0.0f, 0.0f);
    }

    public void set(float w, float h, float x, float y, float ang) {
        Mat4f mat = new Mat4f();
        mat.makeIdent();
        mat.set(0, 0, w);
        mat.set(1, 1, h);

        Mat4f rot = new Mat4f();
        rot.makeIdent();
        float c = (float) Math.cos(ang);
        float s = (float) Math.sin(ang);
        rot.set(0, 0, c);
        rot.set(0, 1, -s);
        rot.set(1, 0, s);
        rot.set(1, 1, c);
        mat.set(mat.mul(rot));

        Mat4f pos = new Mat4f();
        pos.makeIdent();
        pos.set(3, 0, x);
        pos.set(3, 1, y);

        m_transform.set(mat.mul(pos));

        m_w = w;
        m_h = h;
        m_x = x;
        m_y = y;
        m_angle = ang;
    }

    @Override
    public void hide() {}

    @Override
    public void show() {}

    @Override
    public boolean isShown() {
        return false;
    }

    @Override
    public void setRect(float w, float h) {
        set(w, h, m_x, m_y, m_angle);
    }

    @Override
    public void resize(float dw, float dh) {
        float w = m_w + dw;
        float h = m_h + dh;
        set(w, h, m_x, m_y, m_angle);
    }

    @Override
    public void setOrigin(float x, float y) {

        m_ox = x;
        m_oy = y;

        float nox = m_ox / m_w;
        float noy = m_oy / m_h;
        float no[] = new float[] { -nox, -noy, 1.0f - nox, -noy, -nox, 1.0f - noy, 1.0f - nox, 1.0f - noy };
        m_parent.m_aOrigin.putf(m_bufIdx, no);
    }

    @Override
    public void setRotation(float angle) {
        set(m_w, m_h, m_x, m_y, angle);
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
        return m_angle;
    }

    @Override
    public void setPos(float x, float y) {
        set(m_w, m_h, x, y, m_angle);
    }

    @Override
    public void move(float dx, float dy) {
        m_x += dx;
        m_y += dy;
        set(m_w, m_h, m_x, m_y, m_angle);
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

}
