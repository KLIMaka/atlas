package shaders;

import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;

import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;

public class Shaders {

    static public ShaderProgram loadShaderProgram(GL2ES2 gl, String name) {
        ShaderCode vert = ShaderCode.create(gl, GL2.GL_VERTEX_SHADER, 1, Shaders.class,
                new String[] { name + "_v.glsl" });
        ShaderCode frag = ShaderCode.create(gl, GL2.GL_FRAGMENT_SHADER, 1, Shaders.class, new String[] { name
                + "_f.glsl" });
        ShaderProgram prog = new ShaderProgram();
        prog.add(vert);
        prog.add(frag);
        prog.link(gl, System.err);

        return prog;
    }
}
