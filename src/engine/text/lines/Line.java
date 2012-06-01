package engine.text.lines;

import java.util.Iterator;

public class Line implements Iterable<String> {

    private String m_line;
    private Line   m_prev;
    private Line   m_next;

    public Line(String line) {
        m_line = line;
    }

    public Line(String[] lines) {
        int i = 0;
        m_line = lines[i];
        Line cur = this;
        for (i = 1; i < lines.length; i++)
            cur = cur.addAfter(lines[i]);
    }

    public Line first() {

        Line cur = this;
        Line prev = m_prev;
        while (prev != null) {
            cur = prev;
            prev = cur.m_prev;
        }
        return cur;
    }

    public Line last() {

        Line cur = this;
        Line next = m_next;
        while (next != null) {
            cur = next;
            next = cur.m_next;
        }
        return cur;
    }

    public Line next() {
        return m_next;
    }

    public Line ffwd(int i) {

        if (i < 0) return null;

        Line cur = this;
        while (i != 0 && cur != null) {
            cur = cur.next();
            i--;
        }

        return cur;
    }

    public Line rewind(int i) {

        if (i < 0) return null;

        Line cur = this;
        while (i != 0 && cur != null) {
            cur = cur.prev();
            i--;
        }

        return cur;
    }

    public Line prev() {
        return m_prev;
    }

    public void setNext(Line next) {
        m_next = next;
    }

    public void setPrev(Line prev) {
        m_prev = prev;
    }

    public Line insertAfter(Line line) {

        line.setPrev(this);
        if (m_next != null) {
            Line last = line.last();
            next().setPrev(last);
            last.setNext(next());
        }
        setNext(line);

        return line;
    }

    public Line insertBefore(Line line) {

        Line last = line.last();
        last.setNext(this);
        if (m_prev != null) {
            prev().setNext(line);
            line.setPrev(prev());
        }
        setPrev(last);

        return last;
    }

    public Line insertInto(Line line, int pos) {

        if (pos < 0 || pos > m_line.length()) return null;

        String orig = m_line;
        String prefix = orig.substring(0, pos);
        prefix += line.getLine();
        setLine(prefix);

        if (line.next() != null) {
            Line cur = line.last();
            prefix = cur.getLine();
            prefix += orig.substring(pos);
            cur.setLine(prefix);
            cur.linkNext(next());
            linkNext(line.next());
            return line;
        } else {
            setLine(getLine() + orig.substring(pos));
            return this;
        }
    }

    public Line split(int pos) {
        if (pos < 0 || pos > getLine().length()) return this;
        return splitFast(pos);
    }

    public Line splitFast(int pos) {

        String line = getLine();
        String prefix = line.substring(0, pos);
        String posfix = line.substring(pos);
        setLine(prefix);
        return addAfter(posfix);
    }

    public void insertFast(char c, int pos) {

        if (c == '\n' || c == '\r') {
            splitFast(pos);
        } else {
            m_line = m_line.substring(0, pos) + c + m_line.substring(pos);
        }
    }

    public void insert(String str, int pos) {

        if (pos < 0 || pos > m_line.length()) return;

        if (str.matches("^\n\r?$")) {
            splitFast(pos);
        } else if (str.matches("\n\r?")) {
            insertInto(new Line(str), pos);
        } else {
            m_line = m_line.substring(0, pos) + str + m_line.substring(pos);
        }
    }

    public int joinNext() {

        int p = m_line.length();
        String line = m_line + (m_next == null ? "" : m_next.getLine());
        setLine(line);
        dropNext();
        return p;
    }

    public int joinPrev() {

        int p = m_line.length();
        String line = (m_prev == null ? "" : m_prev.getLine()) + m_line;
        setLine(line);
        dropPrev();
        return p;
    }

    public Line dropNext() {
        if (m_next != null) linkNext(m_next.next());
        return this;
    }

    public Line dropPrev() {
        if (m_prev != null) linkPrew(m_prev.prev());
        return this;
    }

    public Line linkNext(Line line) {

        setNext(line);
        if (line != null) line.setPrev(this);
        return line;
    }

    public Line linkPrew(Line line) {

        setPrev(line);
        if (line != null) line.setNext(this);
        return line;
    }

    public Line addAfter(String str) {
        Line line = new Line(str);
        return insertAfter(line);
    }

    public Line addBefore(String str) {
        Line line = new Line(str);
        return insertBefore(line);
    }

    public String getLine() {
        return m_line;
    }

    public void setLine(String line) {
        m_line = line;
    }

    public String join(int n) {

        if (n < 0) return "";

        StringBuilder sb = new StringBuilder(m_line);
        Line cur = next();
        while (cur != null && n-- != 0) {
            sb.append("\n");
            sb.append(cur.getLine());
            cur = cur.next();
        }
        return sb.toString();
    }

    public String join() {

        StringBuilder sb = new StringBuilder(m_line);
        Line cur = next();
        while (cur != null) {
            sb.append("\n");
            sb.append(cur.getLine());
            cur = cur.next();
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return join(20);
    }

    public Iterable<String> limit(final int n) {
        if (n < 0) return null;

        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {

                    private Line m_cur = Line.this;
                    private int  m_n   = n;

                    @Override
                    public boolean hasNext() {
                        return m_cur != null && m_n > 0;
                    }

                    @Override
                    public String next() {
                        m_n--;
                        String ret = m_cur.getLine();
                        m_cur = m_cur.next();
                        return ret;
                    }

                    @Override
                    public void remove() {}
                };
            }
        };
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {

            private Line m_cur = Line.this;

            @Override
            public boolean hasNext() {
                return m_cur != null;
            }

            @Override
            public String next() {
                String ret = m_cur.getLine();
                m_cur = m_cur.next();
                return ret;
            }

            @Override
            public void remove() {}
        };
    }
}
