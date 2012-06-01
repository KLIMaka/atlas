package engine.buffers;

public class bufferConfig {

    public int     size;
    public int     type;
    public boolean normalize;
    public int     stride;
    public int     offset;
    public int     count;

    public bufferConfig(int size, int type, boolean norm, int stride, int offset, int count) {

        this.size = size;
        this.type = type;
        this.normalize = norm;
        this.stride = stride;
        this.offset = offset;

        this.count = count;
    }
}
