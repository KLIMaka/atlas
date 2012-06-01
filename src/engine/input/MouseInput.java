package engine.input;

public interface MouseInput {

    public static final int BUTTOL_LEFT   = 0;
    public static final int BUTTOL_MIDDLE = 1;
    public static final int BUTTOL_RIGHT  = 2;

    public static final int RELEASED      = 0;
    public static final int PRESSED       = 1;

    public boolean isReleaseAction();

    public boolean isPressAction();

    public int x();

    public int y();

    public int z();

    public int button(int button);

    public void reset();
}
