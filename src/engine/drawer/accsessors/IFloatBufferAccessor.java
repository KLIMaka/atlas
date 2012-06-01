package engine.drawer.accsessors;

public interface IFloatBufferAccessor extends IBufferAccsessor {

    public void put4f(int off, float x, float y, float z, float w);

    public void put3f(int off, float x, float y, float z);

    public void put2f(int off, float x, float y);

    public void put1f(int off, float x);

    public void putf(int off, float[] data);

}
