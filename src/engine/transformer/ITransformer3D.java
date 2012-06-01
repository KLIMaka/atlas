package engine.transformer;

public interface ITransformer3D extends ITransformer2D {

    public void translate(float x, float y, float z);

    public void scale(float x, float y, float z);

    public void rotate(float angle, float x, float y, float z);
}
