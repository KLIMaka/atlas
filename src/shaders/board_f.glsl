in vec4 texcoord;

uniform sampler2D Tex1;
uniform sampler2D Tex2;

void main(void)
{
	vec4 t = texture(Tex1, gl_TexCoord[0].st);
	if( length(t) == 0) discard;
	gl_FragColor = texture(Tex1, gl_TexCoord[0].st);
}