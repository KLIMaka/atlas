attribute vec3 a_position;
attribute vec4 a_size;
attribute vec4 a_color;

uniform mat4 mvp_matrix;

varying vec4 color;
varying mat2 rot;
varying vec4 ntc;

void main(void)
{
	float a = a_position.z;
	float c = cos(radians(a));
	float s = sin(radians(a));
	rot = mat2(c, s, -s, c);
	
	vec2 p1 = abs(a_size.xy * vec2(s,c));
	vec2 p2 = abs(a_size.xy * vec2(c,s));
	
	float size = max(p1.x+p1.y, p2.x+p2.y);
	ntc.xy = vec2(size / a_size.xy);
	ntc.zw = (ntc.xy - 1.0) / 2.0;
	
	vec2 cent = vec2(50.0, 50.0);
	vec2 off = (vec2(0.5) - cent / a_size.xy) * a_size.xy * transpose(rot);
	
    gl_Position = vec4(a_position + off, 0.0, 1.0) * mvp_matrix;
    gl_PointSize = size;
    color = a_color;
}