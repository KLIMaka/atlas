package engine.drawer.accsessors;

public interface IByteBufferAccessor extends IBufferAccsessor {

    public void put1ub(int off, int x);

    public void put2ub(int off, int x, int y);

    public void put3ub(int off, int x, int y, int z);

    public void put4ub(int off, int x, int y, int z, int w);

    public void putub(int off, byte[] data);
}
