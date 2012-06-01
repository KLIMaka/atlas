package engine.text.lines;

public class TextRegion {

    public int start;
    public int end;
    public int startLine;
    public int endLine;
    public int startPos;
    public int endPos;

    public int length() {
        return end - start;
    }
}
