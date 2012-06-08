attribute vec2 a_or;
attribute vec4 a_color;

uniform mat4 v_matrix;
uniform mat4 p_matrix;
uniform mat4 m_matrix;

varying vec4 color;

void main(void)
{ 
    gl_Position = vec4(a_or, 0.0, 1.0) * m_matrix * v_matrix * p_matrix;
    color = a_color;
}