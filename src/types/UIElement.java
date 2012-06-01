package types;

import com.jogamp.opengl.util.texture.Texture;

public class UIElement extends DrawableElement {

    private Texture m_tex;
    private float[] m_rect     = { 0, 0, 0, 0 };
    private float[] m_pos      = { 0, 0, 0, 1 };
    private float[] m_addColor = { 0, 0, 0, 0 };
    private float[] m_mulColor = { 1, 1, 1, 1 };

    private String  m_label;
    private int[]   m_labelOff = { 0, 0 };

    protected UIElement(Texture tex, float cx, float cy) {

        m_tex = tex;
        m_rect[0] = tex.getWidth();
        m_rect[1] = tex.getHeight();
        m_rect[2] = cx / m_rect[0];
        m_rect[3] = cy / m_rect[1];
    }

    public void setPos(float x, float y) {
        m_pos[0] = x;
        m_pos[1] = y;
    }

    public void setSize(float x, float y) {
        m_rect[0] = x;
        m_rect[1] = y;
    }

    public void setScale(float scale) {
        m_pos[3] = scale;
    }

    @Override
    public float[] addColor() {
        return m_addColor;
    }

    public void setAddColor(float r, float g, float b, float a) {
        m_addColor[0] = r;
        m_addColor[1] = g;
        m_addColor[2] = b;
        m_addColor[3] = a;
    }

    public void setMulColor(float r, float g, float b, float a) {
        m_mulColor[0] = r;
        m_mulColor[1] = g;
        m_mulColor[2] = b;
        m_mulColor[3] = a;
    }

    @Override
    public float[] mulColor() {
        return m_mulColor;
    }

    @Override
    public Texture colorTex() {
        return m_tex;
    }

    @Override
    public Texture normalTex() {
        return null;
    }

    @Override
    public Texture heightTex() {
        return null;
    }

    @Override
    public float[] worldPos() {
        return m_pos;
    }

    @Override
    public float scale() {
        return m_pos[3];
    }

    @Override
    public float[] rect() {
        return m_rect;
    }

    @Override
    public float[] getAttrib(int idx) {

        switch (idx) {

        case ATTRIB_INDEX:
            return m_idArr;

        case ATTRIB_POS_SCALE:
            return m_pos;

        case ATTRIB_SIZE_CENTER:
            return m_rect;

        case ATTRIB_PRI_COLOR:
            return m_mulColor;

        case ATTRIB_SEC_COLOR:
            return m_addColor;
        }

        return null;
    }

    @Override
    public Texture getTex(int idx) {

        switch (idx) {

        case TEX_COLOR:
            return m_tex;
        }

        return null;
    }

}
