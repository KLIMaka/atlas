package engine.text;

import java.util.ArrayList;

public class MultilineTextEditor {

    private ArrayList<String> m_lines    = new ArrayList<String>();
    private int               m_chars;
    private TextPos           m_caret    = new TextPos();
    private int               m_caretAbs = 0;

    public MultilineTextEditor() {}

    public void setText(String text) {
        clear();
        for (String line : text.split("\n\r?")) {
            addLine(line);
        }
    }

    public void addLine(String line) {
        m_lines.add(line);
        m_chars += line.length();
    }

    public void setLine(int ln, String line) {
        if (ln < 0 || ln >= m_lines.size()) return;
        m_chars += line.length() - m_lines.get(ln).length();
        m_lines.set(ln, line);

    }

    public void clear() {
        m_lines.clear();
        m_chars = 0;
    }

    public void setCaret(int abs) {
        abs = abs < 0 ? 0 : (abs > m_chars ? m_chars : abs);
        m_caretAbs = abs;
        recalcCaretByAbs();
    }

    private void recalcCaretByAbs() {}
}
