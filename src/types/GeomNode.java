package types;

import gleem.linalg.Vec3f;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class GeomNode extends Node {
    private Vec3f m_pos    = new Vec3f();
    private Vec3f m_orient = new Vec3f();
    private Vec3f m_color  = new Vec3f();

    public GeomNode(Node parent, int id) {
        super(parent, id);
    }

    @Override
    public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();

        gl.glTranslatef(m_pos.x(), m_pos.y(), m_pos.z());
        gl.glColor3f(m_color.x(), m_color.y(), m_color.z());

        super.draw(drawable);

        gl.glPopMatrix();
    }

    public void selectDraw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glPushMatrix();

        gl.glTranslatef(m_pos.x(), m_pos.y(), m_pos.z());
        Vec3f idcol = getIdColor();
        gl.glColor3f(idcol.x(), idcol.y(), idcol.z());

        super.selectDraw(drawable);

        gl.glPopMatrix();
    }

    protected Vec3f getOrigin() {
        final Vec3f orig = new Vec3f();
        if (getParent() instanceof GeomNode) {
            return ((GeomNode) getParent()).getWorldPos();
        }

        return orig;
    }

    public Vec3f getWorldPos() {
        Vec3f o = getOrigin();
        o.add(m_pos);
        return o;
    }

    public Vec3f getPos() {
        return m_pos;
    }

    public Vec3f getOrient() {
        return m_orient;
    }

    public Vec3f getColor() {
        return m_color;
    }

    public void setColor(Vec3f color) {
        m_color = color;
    }

    public void setPos(Vec3f pos) {
        m_pos = pos;
    }

    public void setOrient(Vec3f orient) {
        m_orient = orient;
    }
}
