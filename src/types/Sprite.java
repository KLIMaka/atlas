package types;

import java.util.Arrays;

import com.jogamp.opengl.util.texture.Texture;

public class Sprite extends DrawableElement implements Cloneable {

    private Texture m_colorMap;
    private Texture m_normalMap;
    private Texture m_heightMap;

    private float[] m_addColor = { 0.0f, 0.0f, 0.0f, 1.0f };
    private float[] m_mulColor = { 1.0f, 1.0f, 1.0f, 1.0f };

    private float[] m_worldPos = { 0.0f, 0.0f, 0.0f, 1.0f };
    private float[] m_rect     = { 0.0f, 0.0f, 0.0f, 0.0f };

    private float[] m_height   = { 0.0f, 0.0f, 0.0f, 0.0f };

    private float   m_h;
    private float   m_w;

    protected Sprite() {
    }

    protected Sprite(Texture c, Texture n, Texture h, float heigth, float xoff, float yoff,
            float w, float hh) {

        m_colorMap = c;
        m_normalMap = n;
        m_heightMap = h;

        m_w = w == -1.0f ? m_colorMap.getWidth() : w;
        m_h = hh == -1.0f ? m_colorMap.getHeight() : hh;

        m_height[0] = heigth;

        m_rect[0] = m_w;
        m_rect[1] = m_h;
        m_rect[2] = xoff / m_w;
        m_rect[3] = yoff / m_h;
    }

    public void setPos(float x, float y, float z) {

        m_worldPos[0] = x;
        m_worldPos[1] = y;
        m_worldPos[2] = z;
    }

    public void move(float x, float y, float z) {

        m_worldPos[0] += x;
        m_worldPos[1] += y;
        m_worldPos[2] += z;
    }

    public void scale(float s) {
        m_worldPos[3] = s;
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
    protected Sprite clone() {

        try {
            Sprite spr = (Sprite) super.clone();
            spr.m_idArr = Arrays.copyOf(m_idArr, 4);
            spr.m_addColor = Arrays.copyOf(m_addColor, 4);
            spr.m_mulColor = Arrays.copyOf(m_mulColor, 4);
            spr.m_worldPos = Arrays.copyOf(m_worldPos, 4);
            spr.m_rect = Arrays.copyOf(m_rect, 4);
            spr.m_height = Arrays.copyOf(m_height, 4);
            return spr;

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public float[] addColor() {
        return m_addColor;
    }

    @Override
    public float[] mulColor() {
        return m_mulColor;
    }

    @Override
    public Texture colorTex() {
        return m_colorMap;
    }

    @Override
    public Texture normalTex() {
        return m_normalMap;
    }

    @Override
    public Texture heightTex() {
        return m_heightMap;
    }

    @Override
    public float[] worldPos() {
        return m_worldPos;
    }

    public float height() {
        return m_height[0];
    }

    @Override
    public float scale() {
        return m_worldPos[3];
    }

    @Override
    public float[] rect() {
        return m_rect;
    }

    public static final int ATTRIB_HEIGHT = DrawableElement.ATTRIB_LAST + 1;
    public static final int ATTRIB_LAST   = DrawableElement.ATTRIB_LAST + 1;

    public float[] getAttrib(int idx) {

        switch (idx) {

        case ATTRIB_INDEX:
            return m_idArr;

        case ATTRIB_POS_SCALE:
            return m_worldPos;

        case ATTRIB_SIZE_CENTER:
            return m_rect;

        case ATTRIB_PRI_COLOR:
            return m_mulColor;

        case ATTRIB_SEC_COLOR:
            return m_addColor;

        case ATTRIB_HEIGHT:
            return m_height;
        }

        return null;
    }

    @Override
    public Texture getTex(int idx) {

        switch (idx) {

        case TEX_COLOR:
            return m_colorMap;

        case TEX_NORMAL:
            return m_normalMap;

        case TEX_HEIGHT:
            return m_heightMap;
        }

        return null;
    }
}
