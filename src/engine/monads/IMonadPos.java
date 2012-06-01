package engine.monads;

public interface IMonadPos {

    public void setPos(float x, float y);

    public void move(float dx, float dy);

    public float x();

    public float y();
}
