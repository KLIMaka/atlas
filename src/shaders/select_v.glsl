varying out vec4 pos_scale;
varying out vec4 spriteH;

void main(void)
{
	vec4 rect = gl_MultiTexCoord1;
	pos_scale = gl_MultiTexCoord2;
	spriteH = gl_MultiTexCoord3;
	
	vec2 size = rect.xy * pos_scale.w;
	vec2 offset = rect.zw * size;
	
	vec4 nvertex = vec4(gl_Vertex.xy * size + pos_scale.xy - vec2(0.0, pos_scale.z) - offset, gl_Vertex.zw);   
	
    gl_Position = gl_ModelViewProjectionMatrix * nvertex;
    gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_FrontColor = gl_MultiTexCoord4;
}