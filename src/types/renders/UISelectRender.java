package types.renders;

import javax.media.opengl.GLES2;

import types.UIElement;

public class UISelectRender extends BaseSpriteRender {

    public UISelectRender(GLES2 gl) {

        super(gl, "uiselect", new int[] { UIElement.ATTRIB_INDEX, UIElement.ATTRIB_SIZE_CENTER,
                UIElement.ATTRIB_POS_SCALE }, new String[] { "colorTex" }, new int[] { UIElement.TEX_COLOR });
    }
}
