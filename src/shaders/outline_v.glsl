uniform float scale;
uniform vec3 worldPos;
uniform vec4 rect;

void main(void)
{
	vec2 size = rect.xy * scale;
	vec2 offset = rect.zw * size;
	
	vec4 nvertex = vec4(gl_Vertex.xy * size + worldPos.xy - vec2(0.0, worldPos.z) - offset, gl_Vertex.zw);   
    gl_Position = gl_ModelViewProjectionMatrix * nvertex;
    gl_TexCoord[0] = gl_MultiTexCoord0;
}