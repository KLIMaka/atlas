package engine.text;

import java.util.ArrayList;
import java.util.Arrays;

public class LinesHolder {

    private ArrayList<String> m_lines      = new ArrayList<String>();
    private int               m_chars      = 0;
    private int               m_linesCount = 0;
    private int[]             m_lineStart  = new int[32];

    public void clear() {

        m_lines.clear();
        m_chars      = 0;
        m_linesCount = 0;
        m_lineStart  = new int[32];
    }

    public void addLine(String line) {

        m_lines.add(line);
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
        for (String line : m_lines) {
            m_lineStart[ln] = size;
            ln++;
            size += line.length();
        }
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

    public int getAbsByTextPos(int line, int pos) {

        line = line < 0 ? 0 : (line >= m_linesCount ? m_linesCount - 1 : line);
        int len = m_lines.get(line).length();
        pos = pos < 0 ? 0 : (pos > len ? len : pos);
        return m_lineStart[line] + pos;
    }

    public String getRegion(int start, int len) {

        int end = start + len;
        if (end == start) return "";

        StringBuilder sb = new StringBuilder();
        TextPos stp      = getTextPosByAbs(start);
        TextPos etp      = getTextPosByAbs(end);
        int begin        = stp.pos;
        for (int i = stp.line; i < etp.line; i++) {
            sb.append(m_lines.get(i).substring(begin));
            sb.append("\n");
            begin = 0;
        }
        sb.append(m_lines.get(etp.line).substring(begin, etp.pos));

        return sb.toString();
    }

    public String getRegion(int startLine, int startPos, int endLine, int endPos) {

        int start = getAbsByTextPos(startLine, startPos);
        int len = getAbsByTextPos(endLine, endPos) - start;
        return getRegion(start, len);
    }

    public void setRegion(int start, int end, String str) {

        String[] lines = str.split("\n\r?");
        TextPos stp    = getTextPosByAbs(start);
        TextPos etp    = getTextPosByAbs(end);

        for (int i = stp.line + 1; i < etp.line; i++)
            m_lines.remove(i);

        String a = m_lines.get(stp.line).substring(0, stp.pos) + lines[0];

    }

    @Override
    public String toString() {

        if (m_chars == 0) return "";

        StringBuilder sb = new StringBuilder();
        for (String line : m_lines) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

}
