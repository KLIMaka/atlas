void main(void)
{
	vec4 pos_scale = gl_MultiTexCoord1;
	vec4 rect = gl_MultiTexCoord2;
	
	vec2 size = rect.xy * pos_scale.w;
	vec2 offset = rect.zw * rect.xy * (pos_scale.w - 1);
	vec4 nvertex = vec4(gl_Vertex.xy * size + pos_scale.xy - vec2(0.0, pos_scale.z) - offset, gl_Vertex.zw);   

    gl_Position = gl_ModelViewProjectionMatrix * nvertex;
    gl_TexCoord[0] = gl_MultiTexCoord0;
}