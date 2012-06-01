package types.renders;

import javax.media.opengl.GLES2;

import types.DrawableElement;
import types.PanelElement;

public class PanelSelectRender extends BaseBufferRender {

    public PanelSelectRender(GLES2 gl) {
        super(gl, "ui_panelselect", new int[] { PanelElement.ATTRIB_INDEX }, new String[] { "colorTex" },
                new int[] { PanelElement.TEX_COLOR });
    }

    @Override
    public void draw(DrawableElement elem) {

        PanelElement panel = (PanelElement) elem;

        gl().glPushMatrix();
        gl().glTranslatef(panel.worldPos()[0], panel.worldPos()[1], 0.0f);

        super.draw(panel);
        for (PanelElement child : panel.childs()) {
            draw(child);
        }

        gl().glPopMatrix();
    }

}
