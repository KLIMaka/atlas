package types.renders;

import java.nio.Buffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLES2;

import types.Sprite;

public class SelectRender extends BaseSpriteRender {

    public SelectRender(GLES2 gl) {

        super(gl, "select", new int[] { Sprite.ATTRIB_SIZE_CENTER, Sprite.ATTRIB_POS_SCALE, Sprite.ATTRIB_HEIGHT,
                Sprite.ATTRIB_INDEX }, new String[] { "colorTex", "heightTex" }, new int[] { Sprite.TEX_COLOR,
                Sprite.TEX_HEIGHT });
    }

    public int getId(int x, int y) {

        final int[] point = new int[1];
        final Buffer buf = IntBuffer.wrap(point);
        gl().glReadPixels(x, y, 1, 1, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, buf);

        return point[0];
    }

}
