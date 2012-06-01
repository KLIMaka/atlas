package types;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import com.jogamp.opengl.util.texture.Texture;

public abstract class DrawableElement {

    protected int     m_id;
    protected float[] m_idArr  = new float[4];
    protected Script  m_script = null;

    public int id() {
        return m_id;
    }

    protected void setID(int id) {
        m_id = id;
        m_idArr[0] = (m_id & 0xff) / 255.0f;
        m_idArr[1] = ((m_id >> 8) & 0xff) / 255.0f;
        m_idArr[2] = ((m_id >> 16) & 0xff) / 255.0f;
        m_idArr[3] = ((m_id >> 24) & 0xff) / 255.0f;
    }

    public float[] idv() {
        return m_idArr;
    }

    public void setScript(Script scr) {

        m_script = scr;
        m_script.inject(this, "this");
        m_script.init();
    }

    public void post(String msg) {
        if (m_script != null) {
            m_script.exec(msg);
        }
    }

    public Varargs call(String name, LuaTable arg) {
        if (m_script != null) {
            int n = arg.getn().toint();
            LuaValue[] lvs = new LuaValue[n];
            for (int i = 0; i < n; i++)
                lvs[i] = arg.get(i + 1);
            return m_script.call(name, LuaValue.varargsOf(lvs));
        }
        return null;
    }

    abstract public float[] addColor();

    abstract public float[] mulColor();

    abstract public Texture colorTex();

    abstract public Texture normalTex();

    abstract public Texture heightTex();

    abstract public float[] worldPos();

    abstract public float scale();

    abstract public float[] rect();

    public static final int ATTRIB_INDEX       = 0;
    public static final int ATTRIB_POS_SCALE   = 1;
    public static final int ATTRIB_SIZE_CENTER = 2;
    public static final int ATTRIB_PRI_COLOR   = 3;
    public static final int ATTRIB_SEC_COLOR   = 4;

    public static final int ATTRIB_LAST        = 4;

    abstract public float[] getAttrib(int idx);

    public static final int TEX_COLOR  = 0;
    public static final int TEX_NORMAL = 1;
    public static final int TEX_HEIGHT = 2;

    public static int       TEX_LAST   = 2;

    abstract public Texture getTex(int idx);

}
