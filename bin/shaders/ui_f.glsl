uniform sampler2D colorTex;

void main(void)
{
	vec2 texcoord = gl_TexCoord[0].st;
	vec4 col = texture2D(colorTex, texcoord);
	gl_FragColor = col;

}