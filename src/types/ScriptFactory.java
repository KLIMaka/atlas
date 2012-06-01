package types;

public class ScriptFactory {

    private Engine m_engine;

    public ScriptFactory(Engine engine) {
        m_engine = engine;
    }

    public Script create(String name) {
        Script scr = new Script(name);
        scr.inject(m_engine, "engine");
        return scr;
    }

}
