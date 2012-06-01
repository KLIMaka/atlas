package engine.monads;

public interface IMonadRect {

    public void setRect(float w, float h);

    public void resize(float dw, float dh);

    public void setOrigin(float x, float y);

    public void setRotation(float angle);

    public float height();

    public float width();

    public float xOrigin();

    public float yOrigin();

    public float angle();

}
