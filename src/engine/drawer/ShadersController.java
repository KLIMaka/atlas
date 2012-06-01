package engine.drawer;

import java.util.HashMap;
import java.util.Map;

public class ShadersController {

    private Map<String, ShaderInfo> m_shaders = new HashMap<String, ShaderInfo>();

    public ShadersController() {}

    public ShaderInfo registerShader(String name) {
        ShaderInfo shader = m_shaders.get(name);
        if (shader == null) {
            shader = new ShaderInfo(name);
            m_shaders.put(name, shader);
        }
        return shader;
    }
}
