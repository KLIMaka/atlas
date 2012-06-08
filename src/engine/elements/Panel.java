package engine.elements;

import engine.monads.IMonadDrawable;
import engine.monads.IMonadID;
import engine.monads.IMonadPos;
import engine.monads.IMonadRect;
import engine.transformer.Transformation;

public class Panel implements IElement, IMonadID, IMonadPos, IMonadRect, IMonadDrawable {

    private PanelFactory   m_parent;
    protected int            m_bufIdx;

    private float          m_w = 1.0f, m_h = 1.0f;
    private float          m_x, m_y;
    private float          m_angle;
    private float          m_ox, m_oy;
    private int            m_idx;

    protected Transformation m_transform = new Transformation();

    protected Panel(PanelFactory parent, int idx) {
        m_parent = parent;
        m_bufIdx = idx;
        
        float no[] = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f , 1.0f };
        m_parent.m_aOrigin.putf(m_bufIdx, no);
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
        m_transform.scale(w / m_w, h / m_h);
        m_w = w;
        m_h = h;
    }

    @Override
    public void resize(float dw, float dh) {
        float w = m_w + dw;
        float h = m_h + dh;
        m_transform.scale(w / m_w, h / m_h);
        m_w = w;
        m_h = h;
    }

    @Override
    public void setOrigin(float x, float y) {

        if (x != m_ox || y != m_oy) {
            m_ox = x;
            m_oy = y;

            float nox = m_ox / m_w;
            float noy = m_oy / m_h;
            float no[] = new float[] { -nox, -noy, 1.0f - nox, -noy, -nox, 1.0f - noy, 1.0f - nox, 1.0f - noy };
            m_parent.m_aOrigin.putf(m_bufIdx, no);
        }
    }

    @Override
    public void setRotation(float angle) {
        float da = angle - m_angle;
        m_transform.rotate(da);
        m_angle += da;
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
        return 0;
    }

    @Override
    public void setPos(float x, float y) {
        float dx = x - m_x;
        float dy = y - m_y;
        m_transform.translate(x, y);
        m_x += dx;
        m_y += dy;
    }

    @Override
    public void move(float dx, float dy) {
    	m_transform.translate(dx, dy);
        m_x += dx;
        m_y += dy;
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
