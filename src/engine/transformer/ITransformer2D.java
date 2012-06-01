package engine.transformer;

public interface ITransformer2D extends ITransformer {

    public void translate(float x, float y);

    public void scale(float x, float y);

    public void rotate(float angle);

}
