package types;

import java.awt.Cursor;
import java.awt.Frame;

import engine.input.AWTMouseControl;


public class Engine {

    public AWTMouseControl mouse;
    public Frame          frame;
    public int            screenW;
    public int            screenH;
    public UIFactory      ui;
    public ScriptFactory  scripts;

    public Engine() {
    }

    public void setScreenSize(int w, int h) {
        screenW = w;
        screenH = h;
    }

    public void setCursor(int cursor) {
        frame.setCursor(Cursor.getPredefinedCursor(cursor));
    }

    public int one(int a) {
        return 1;
    }

}
