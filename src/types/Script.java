package types;

import java.io.FileReader;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public class Script {

    private String              m_src;
    private Bindings            m_bindings;
    private CompiledScript      m_script;
    private LuaValue            m_handler;

    private static ScriptEngine engine = new ScriptEngineManager().getEngineByExtension(".lua");
    static {
        org.luaj.vm2.luajc.LuaJC.install();
    }

    static public Compilable getLuaEngine() {
        return (Compilable) engine;
    }

    public Script(String fname) {

        m_bindings = engine.createBindings();
        m_src = fname;
    }

    public Varargs call(String name, Varargs arg) {

        LuaValue func = (LuaValue) m_bindings.get(name);
        return func.invoke(arg);
    }

    public void exec(String fname) {

        try {
            if (m_handler != null) {
                m_handler.call(LuaString.valueOf(fname));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in " + m_src + " " + fname + "() : " + e.getMessage());
        }
    }

    public void inject(Object obj, String name) {
        m_bindings.put(name, obj);
    }

    public void init() {
        try {
            m_script = getLuaEngine().compile(new FileReader(m_src));
            m_script.eval(m_bindings);
            m_handler = (LuaValue) m_bindings.get("HANDLE");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
