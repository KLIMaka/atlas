package engine.buffers;

import java.util.List;

import engine.drawer.accsessors.IBufferAccsessor;

public interface IDrawBuffer {

    public void addComponent(int size, int type, int count, boolean norm);

    public List<bufferConfig> getConfig();

    public void allocate(int usage);

    public int size();

    public IBufferAccsessor getAccsessor(int comp, int offset);

    public IBufferAccsessor getAccsessor(int comp);

    public void bind();

    public void update();
}
