package engine.text.lines;

import java.util.Arrays;
import java.util.Iterator;

public class LinesHolder implements Iterable<String> {

    private Line  m_linesHead  = null;
    private Line  m_linesEnd   = null;
    private int   m_chars      = 0;
    private int   m_linesCount = 0;
    private int[] m_lineStart  = new int[32];

    public void clear() {

        m_linesHead = null;
        m_linesEnd = m_linesHead;
        m_chars = 0;
        m_linesCount = 0;
        m_lineStart = new int[32];
    }

    public void addText(String text) {
        String[] lines = text.split("\n\r?", -1);
        joinLine(lines[0]);
        for (int i = 1; i < lines.length; i++)
            addLine(lines[i]);
    }

    protected void joinLine(String line) {

        if (m_linesEnd == null) {
            m_linesHead = new Line(line);
            m_linesEnd = m_linesHead;
            m_linesCount++;
            m_lineStart[m_linesCount - 1] = m_chars;
            m_chars += line.length();
        } else {
            m_linesEnd.setLine(m_linesEnd.getLine() + line);
        }
    }

    protected void addLine(String line) {

        if (m_linesEnd == null) {
            m_linesHead = new Line(line);
            m_linesEnd = m_linesHead;
        } else {
            m_linesEnd = m_linesEnd.addAfter(line);
        }
        m_linesCount++;
        grow();
        m_lineStart[m_linesCount - 1] = m_chars;
        m_chars += line.length() + 1;
    }

    protected void grow() {

        if (m_lineStart.length <= m_linesCount) {
            m_lineStart = Arrays.copyOf(m_lineStart, m_lineStart.length * 2);
        }
    }

    protected void recalcSizes() {

        int ln = 0;
        int size = 0;
        for (String line : m_linesHead) {
            m_lineStart[ln] = size;
            ln++;
            size += line.length() + 1;
        }
        m_chars = size;
    }

    public TextPos getTextPosByAbs(int abs) {

        abs = abs < 0 ? 0 : (abs >= m_chars ? m_chars - 1 : abs);
        int line = Arrays.binarySearch(m_lineStart, 0, m_linesCount, abs);
        line = line < 0 ? -line - 2 : line;
        int pos = abs - m_lineStart[line];
        return new TextPos(line, pos);
    }

    public int getAbsByTextPos(TextPos tp) {
        return getAbsByTextPos(tp.line, tp.pos);
    }

    public int getLineLength(int ln) {
        return ln == m_linesCount - 1 ? m_linesEnd.getLine().length() : m_lineStart[ln + 1] - m_lineStart[ln];
    }

    public int linesCount() {
        return m_linesCount;
    }

    public int charsCount() {
        return m_chars;
    }

    public int getAbsByTextPos(int line, int pos) {

        line = line < 0 ? 0 : (line >= m_linesCount ? m_linesCount - 1 : line);
        int len = getLineLength(line);
        pos = pos < 0 ? 0 : (pos > len ? len : pos);
        return m_lineStart[line] + pos;
    }

    public String getRegion(TextRegion region) {

        if (region.length() == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        int begin = region.startPos;
        Line cur = m_linesHead.ffwd(region.startLine);
        for (int i = region.startLine; i < region.endLine; i++, cur = cur.next()) {
            sb.append(cur.getLine().substring(begin));
            sb.append("\n");
            begin = 0;
        }
        sb.append(cur.getLine().substring(begin, region.endPos));

        return sb.toString();
    }

    public void remove(TextPos stp, TextPos etp) {
        remove_(computeRegion(stp.line, stp.pos, etp.line, etp.pos));
    }

    public void remove(int sline, int spos, int eline, int epos) {
        remove_(computeRegion(sline, spos, eline, epos));
    }

    public void remove(int start, int end) {
        remove_(computeRegion(start, end));
    }

    protected Line remove_(TextRegion region) {

        Line first = m_linesHead.ffwd(region.startLine);
        Line last = first.ffwd(region.endLine - region.startLine);

        if (first != last) {
            String line = first.getLine();
            line = line.substring(0, region.startPos);
            line += last.getLine().substring(region.endPos);
            first.setLine(line);
            first.linkNext(last.next());
        } else {
            String line = first.getLine();
            first.setLine(line.substring(0, region.startPos) + line.substring(region.endPos));
        }

        recalcSizes();

        return first;
    }

    public void insert(TextPos tp, String str) {
        insert(getAbsByTextPos(tp), str);
    }

    public void insert(int pos, String str) {

        Line lines = new Line(str.split("\n\r?", -1));
        TextPos stp = getTextPosByAbs(pos);
        Line first = m_linesHead.ffwd(stp.line);
        first.insertInto(lines, stp.pos);
        recalcSizes();
    }

    public void replace(int start, int end, String str) {
        replace(computeRegion(start, end), str);
    }

    public void replace(int sline, int spos, int eline, int epos, String str) {
        replace(computeRegion(sline, spos, eline, epos), str);
    }

    public void replace(TextPos stp, TextPos etp, String str) {
        replace(computeRegion(stp.line, stp.pos, etp.line, etp.pos), str);
    }

    public void replace(TextRegion region, String str) {

        Line lines = new Line(str.split("\n\r?", -1));
        remove_(region).insertInto(lines, region.startPos);
        recalcSizes();
    }

    public TextRegion computeRegion(int start, int end) {

        TextRegion tr = new TextRegion();
        tr.start = start < end ? start : end;
        tr.end = end > start ? end : start;

        TextPos tp = getTextPosByAbs(tr.start);
        tr.startLine = tp.line;
        tr.startPos = tp.pos;

        tp = getTextPosByAbs(tr.end);
        tr.endLine = tp.line;
        tr.endPos = tp.pos;

        return tr;
    }

    public TextRegion computeRegion(int sline, int spos, int eline, int epos) {

        int tmp = 0;
        if (sline > eline || (sline == eline && spos > epos)) {
            tmp = sline;
            sline = eline;
            eline = tmp;
            tmp = spos;
            spos = epos;
            epos = tmp;
        }

        TextRegion tr = new TextRegion();
        tr.startLine = sline;
        tr.startPos = spos;
        tr.endLine = eline;
        tr.endPos = epos;
        tr.start = getAbsByTextPos(sline, spos);
        tr.end = getAbsByTextPos(eline, epos);

        return tr;
    }

    @Override
    public String toString() {

        if (m_chars == 0)
            return "";
        return m_linesHead.join();
    }

    @Override
    public Iterator<String> iterator() {
        return m_linesHead.iterator();
    }

    public Iterable<String> range(final int start, final int len) {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return m_linesHead.ffwd(start).limit(len).iterator();
            }
        };
    }

}
