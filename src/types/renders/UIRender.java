package types.renders;

import javax.media.opengl.GLES2;

import types.UIElement;

public class UIRender extends BaseSpriteRender {

    public UIRender(GLES2 gl) {
        super(gl, "ui", new int[] { UIElement.ATTRIB_POS_SCALE, UIElement.ATTRIB_SIZE_CENTER },
                new String[] { "colorTex" }, new int[] { UIElement.TEX_COLOR });
    }

}
