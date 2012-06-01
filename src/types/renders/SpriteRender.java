package types.renders;

import javax.media.opengl.GLES2;

import types.Sprite;

public class SpriteRender extends BaseSpriteRender {

    private int m_lightPos;

    public SpriteRender(GLES2 gl) {

        super(gl, "world_pos", new int[] { Sprite.ATTRIB_SIZE_CENTER, Sprite.ATTRIB_POS_SCALE, Sprite.ATTRIB_PRI_COLOR,
                Sprite.ATTRIB_SEC_COLOR, Sprite.ATTRIB_HEIGHT }, new String[] { "colorTex", "normalTex", "heightTex" },
                new int[] { Sprite.TEX_COLOR, Sprite.TEX_NORMAL, Sprite.TEX_HEIGHT });

        m_lightPos = gl().glGetUniformLocation(getShader().program(), "lightPos");
    }

    public void setLightPos(float x, float y, float z) {
        gl().glUniform3f(m_lightPos, x, y, z);
    }

}
