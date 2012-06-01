package engine.drawer;

import java.util.HashMap;
import java.util.Map;

import shaders.Shaders;

import com.jogamp.opengl.util.glsl.ShaderProgram;

import engine.AtlasEngine;

public class ShaderInfo {

    private ShaderProgram        m_shader;
    private String               m_name;
    private Map<String, Integer> m_uniformLocations = new HashMap<String, Integer>();

    public ShaderInfo(String name) {
        m_name = name;
        m_shader = Shaders.loadShaderProgram(AtlasEngine.gl, name);
    }

    public String getName() {
        return m_name;
    }

    public ShaderProgram getShader() {
        return m_shader;
    }

    public void bind() {
        m_shader.useProgram(AtlasEngine.gl, true);
    }

    public void unbind() {
        m_shader.useProgram(AtlasEngine.gl, false);
    }

    public int getLocation(String name) {
        Integer loc = m_uniformLocations.get(name);
        if (loc == null) {
            loc = AtlasEngine.gl.glGetUniformLocation(m_shader.program(), name);
            if (loc == -1) System.err.println("unknown UniformLocation " + name + " in shader " + m_name);
            m_uniformLocations.put(name, loc);
        }
        return loc;
    }

    public void uniform(String name, float x) {
        AtlasEngine.gl.glUniform1f(getLocation(name), x);
    }

    public void uniform(String name, int x) {
        AtlasEngine.gl.glUniform1i(getLocation(name), x);
    }

    public void uniform(String name, float x, float y) {
        AtlasEngine.gl.glUniform2f(getLocation(name), x, y);
    }

    public void uniform(String name, float x, float y, float z, float w) {
        AtlasEngine.gl.glUniform4f(getLocation(name), x, y, z, w);
    }

    public void uniform(String name, float[] mat) {
        AtlasEngine.gl.glUniformMatrix4fv(getLocation(name), 1, true, mat, 0);
    }
}
