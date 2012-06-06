attribute vec2 a_or;
attribute vec4 a_pos_size;
attribute vec2 a_tc;

uniform mat4 v_matrix;
uniform mat4 p_matrix;
uniform int startLine;
uniform int curLine;
uniform float lineHeight;
uniform vec2 clipWindow;

varying vec2 tc;

void main(void)
{
	vec2 lineOff = vec2(0.0, (curLine - startLine) * lineHeight);
	vec2 pos = vec2(a_or * a_pos_size.zw + a_pos_size.xy + lineOff);
	vec2 cpos = clamp(pos, vec2(0.0), clipWindow); 
		
    gl_Position = vec4(cpos, 0.0, 1.0) * v_matrix * p_matrix;
    tc = a_tc - vec2(8.0, 16.0) * ((pos - cpos) / a_pos_size.zw) * (1.0 / 512.0);
}