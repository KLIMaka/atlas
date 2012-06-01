package engine.transformer;

public interface ITransformer {

    public void scale(float scale);

    public void push();

    public void pop();

    public void reset();

    public float[] get();

    public void set(float[] data);

}
