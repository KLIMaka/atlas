attribute vec2 a_or;
attribute vec3 a_position;
attribute vec4 a_size;
attribute vec4 a_color;

uniform mat4 v_matrix;
uniform mat4 p_matrix;

varying vec4 color;

void main(void)
{ 
    gl_Position = vec4(a_or*a_size + a_position.xy, 0.0, 1.0) * v_matrix * p_matrix;
    color = a_color;
}