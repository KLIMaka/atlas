package engine.text;

public class charStruct {

    public int     x, y;
    public int     w, h;
    public int     xOff, yOff;
    public int     xAdv;
    public int     chnl, page;
    public float[] texCoords = new float[8];

    public void getTexCoords(float ww, float hh) {

        float top = y / hh;
        float bottom = (y + h) / hh;
        float left = x / ww;
        float right = (x + w) / ww;

        texCoords[0] = left;
        texCoords[1] = top;
        texCoords[2] = right;
        texCoords[3] = top;
        texCoords[4] = left;
        texCoords[5] = bottom;
        texCoords[6] = right;
        texCoords[7] = bottom;
    }
}
