package types;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class TextureManager {

    Map<String, Texture> m_texs = new HashMap<String, Texture>();

    public Texture       default_normal_vertical;
    public Texture       default_height_vertical;
    public Texture       default_color;

    public TextureManager() {
        loadDefaults();
    }

    private void loadDefaults() {

        default_normal_vertical = get("data/def_tex/def_n_v.png");
        default_height_vertical = get("data/def_tex/def_h_v.png");
        default_color = get("data/def_tex/def_c.png");
    }

    public Texture get(String name) {

        try {
            Texture tex = m_texs.get(name);
            if (tex == null) {
                tex = TextureIO.newTexture(new File(name), false);
                m_texs.put(name, tex);
            }
            return tex;

        } catch (Exception e) {
            return null;
        }
    }
}
