package engine.transformer;

import gleem.linalg.Mat4f;
import gleem.linalg.Rotf;
import gleem.linalg.Vec3f;

import java.util.Stack;

public class DefaultTransformer implements ITransformer3D {

    private Mat4f        m_current;
    private Stack<Mat4f> m_stack  = new Stack<Mat4f>();
    private float        m_data[] = new float[16];

    public DefaultTransformer() {
        m_current = new Mat4f();
        m_current.makeIdent();
        push();
    }

    @Override
    public void translate(float x, float y) {
        translate(x, y, 0.0f);
    }

    @Override
    public void scale(float x, float y) {
        scale(x, y, 1.0f);
    }

    @Override
    public void rotate(float angle) {
        rotate(angle, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void scale(float scale) {
        scale(scale, scale, scale);

    }

    @Override
    public void push() {
        m_stack.push(new Mat4f(m_current));
        m_current = m_stack.peek();
    }

    @Override
    public void pop() {
        m_stack.pop();
        m_current = m_stack.peek();
    }

    @Override
    public void reset() {
        m_current.makeIdent();
    }

    @Override
    public void translate(float x, float y, float z) {
        Mat4f m = new Mat4f();
        m.makeIdent();
        m.setTranslation(new Vec3f(x, y, z));
        m_current.mul(m_current, m);
    }

    @Override
    public void scale(float x, float y, float z) {
        m_current.set(0, 0, m_current.get(0, 0) * x);
        m_current.set(1, 1, m_current.get(1, 1) * y);
        m_current.set(2, 2, m_current.get(2, 2) * z);
    }

    @Override
    public void rotate(float angle, float x, float y, float z) {
        Mat4f m = new Mat4f();
        m.makeIdent();
        m.setRotation(new Rotf(new Vec3f(x, y, z), angle));
        m_current.set(m_current.mul(m));
    }

    public float[] get() {
        m_current.getColumnMajorData(m_data);
        return m_data;
    }

    @Override
    public void set(float[] data) {
        for (int i = 0; i < 16; i++) {
            m_current.set(i / 4, i % 4, data[i]);
        }
    }
}
