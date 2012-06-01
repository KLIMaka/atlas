package engine.text;

public class TextPos {

    public int line;
    public int pos;

    public TextPos(int line, int pos) {
        this.line = line;
        this.pos = pos;
    }

    public TextPos() {}

    @Override
    public String toString() {
        return "[" + line + ":" + pos + "]";
    }

}
