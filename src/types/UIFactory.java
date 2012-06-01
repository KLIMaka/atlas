package types;

import com.jogamp.opengl.util.texture.Texture;

public class UIFactory extends ElementsFactory {

    protected TextureManager m_TM;

    public UIFactory(TextureManager tm) {
        m_TM = tm;
    }

    public UIElement create(String name) {

        Texture tex = m_TM.get(name);
        UIElement ui = new UIElement(tex, tex.getWidth() / 2.0f, tex.getHeight() / 2.0f);
        put(ui);
        return ui;
    }

    public PanelElement createPanel(String name, float d) {

        Texture tex = m_TM.get(name);
        PanelElement panel = new PanelElement(tex, d);
        put(panel);
        return panel;
    }

}
