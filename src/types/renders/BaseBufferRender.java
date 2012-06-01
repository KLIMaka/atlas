package types.renders;

import javax.media.opengl.GLES2;

import main.Attribs;
import types.DrawableElement;
import types.PanelElement;

public class BaseBufferRender extends Render {

    public BaseBufferRender(GLES2 gl, String shader, int[] tca, String[] txn, int[] txi) {
        super(gl, shader, tca, txn, txi);
    }

    @Override
    public void begin() {

        gl().glEnableVertexAttribArray(Attribs.vertex);
        gl().glEnableVertexAttribArray(Attribs.attrib1);
        super.begin();
    }

    @Override
    public void end() {

        super.end();
        gl().glDisableVertexAttribArray(Attribs.vertex);
        gl().glDisableVertexAttribArray(Attribs.attrib1);
    }

    @Override
    public void draw(DrawableElement elem) {

        PanelElement bufElem = (PanelElement) elem;

        prepare(bufElem);
        bufElem.bindBuffers(gl());
        gl().glDrawElements(bufElem.getMode(), bufElem.getCount(), bufElem.getType(), bufElem.getIndices());

    }

}
