package engine.transformer;

import gleem.linalg.Mat4f;
import gleem.linalg.Rotf;
import gleem.linalg.Vec3f;

public class Transformation {

    private Mat4f   m_mat  = new Mat4f();
    private float[] m_data = new float[16];

    public Transformation() {
        m_mat.makeIdent();
    }

    public void setScale(float x, float y) {
        m_mat.set(0, 0, x);
        m_mat.set(1, 1, y);
    }

    public void setScale(float x, float y, float z) {
        m_mat.set(0, 0, x);
        m_mat.set(1, 1, y);
        m_mat.set(2, 2, z);
    }

    public void scale(float scale) {
        scale(scale, scale, scale);
    }

    public void scale(float x, float y) {
        m_mat.set(0, 0, m_mat.get(0, 0) * x);
        m_mat.set(1, 1, m_mat.get(1, 1) * y);
    }

    public void scale(float x, float y, float z) {
        m_mat.set(0, 0, m_mat.get(0, 0) * x);
        m_mat.set(1, 1, m_mat.get(1, 1) * y);
        m_mat.set(2, 2, m_mat.get(2, 2) * z);
    }

    public void translate(float x, float y) {
        translate(x, y, 0.0f);
    }

    public void translate(float x, float y, float z) {
        //Mat4f m = new Mat4f();
        //m.makeIdent();
        //m.setTranslation(new Vec3f(x, y, z));
        //m_mat.mul(m_mat, m);
    	m_mat.setTranslation(new Vec3f(x,y,z));
    }

    public void rotate(float angle) {
        rotate(angle, 0.0f, 0.0f, 1.0f);
    }

    public void rotate(float angle, float x, float y, float z) {
        Mat4f m = new Mat4f();
        m.makeIdent();
        m.setRotation(new Rotf(new Vec3f(x, y, z), angle));
        m_mat.set(m_mat.mul(m));
    }

    public float[] get() {
        m_mat.getColumnMajorData(m_data);
        return m_data;
    }
}
