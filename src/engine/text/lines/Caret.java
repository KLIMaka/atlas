package engine.text.lines;

import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_UP;

import java.awt.event.KeyEvent;

public class Caret {

    private Line    m_line;
    private TextPos m_pos    = new TextPos();
    private int     m_refPos = 0;

    public Caret(Line line) {
        m_line = line;
    }

    protected void prevLine() {
        m_line = m_line.prev();
        m_pos.line--;
    }

    protected void nextLine() {
        m_line = m_line.next();
        m_pos.line++;
    }

    public void left() {

        if (m_pos.pos != 0) {
            m_pos.pos--;
            m_refPos = m_pos.pos;
        } else if (m_pos.pos == 0 && m_line.prev() != null) {
            prevLine();
            m_pos.pos = m_line.getLine().length();
            m_refPos = m_pos.pos;
        }
    }

    public void right() {

        int limit = m_line.getLine().length();
        if (m_pos.pos != limit) {
            m_pos.pos++;
            m_refPos = m_pos.pos;
        } else if (m_pos.pos == limit && m_line.next() != null) {
            nextLine();
            m_pos.pos = 0;
            m_refPos = m_pos.pos;
        }
    }

    public void up() {

        if (m_line.prev() != null) {
            prevLine();
            int limit = m_line.getLine().length();
            m_pos.pos = m_refPos > limit ? limit : m_refPos;
        }
    }

    public void down() {

        if (m_line.next() != null) {
            nextLine();
            int limit = m_line.getLine().length();
            m_pos.pos = m_refPos > limit ? limit : m_refPos;
        }
    }

    public void backspace() {

        if (m_pos.pos != 0) {
            String line = m_line.getLine();
            m_line.setLine(line.substring(0, m_pos.pos - 1) + line.substring(m_pos.pos));
            m_pos.pos--;
            m_refPos--;
        } else if (m_pos.pos == 0 && m_pos.line != 0) {
            int pos = m_line.joinPrev();
            m_pos.line--;
            m_pos.pos = pos;
            m_refPos = m_pos.pos;
        }
    }

    public void put(char c) {

        if (c == '\n') {
            m_line = m_line.splitFast(m_pos.pos);
            m_pos.line++;
            m_pos.pos = 0;
            m_refPos = 0;
        } else {
            m_line.insertFast(c, m_pos.pos++);
            m_refPos = m_pos.pos;
        }
    }

    public void key(KeyEvent e) {

        switch (e.getKeyCode()) {

        case VK_UP:
            up();
            break;

        case VK_DOWN:
            down();
            break;

        case VK_LEFT:
            left();
            break;

        case VK_RIGHT:
            right();
            break;

        case VK_BACK_SPACE:
            backspace();
            break;

        default:
            put(e.getKeyChar());
        }
    }

}
