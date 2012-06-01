package types;

import com.jogamp.opengl.util.texture.Texture;

public class SpriteFactory extends ElementsFactory {

    protected TextureManager m_TM;

    public SpriteFactory(TextureManager tm) {
        m_TM = tm;
    }

    public Sprite create(String name, float heigth, float xoff, float yoff, float w, float h) {

        Texture ct = m_TM.get(name + "_c.png");
        Texture nt = m_TM.get(name + "_n.png");
        Texture ht = m_TM.get(name + "_h.png");

        ct = ct == null ? m_TM.default_color : ct;
        nt = nt == null ? m_TM.default_normal_vertical : nt;
        ht = ht == null ? m_TM.default_height_vertical : ht;

        Sprite spr = new Sprite(ct, nt, ht, heigth, xoff, yoff, w, h);
        put(spr);
        return spr;
    }

    public Sprite clone(Sprite spr) {

        Sprite nspr = spr.clone();
        put(nspr);
        return nspr;
    }
}
