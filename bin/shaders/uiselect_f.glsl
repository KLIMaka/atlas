uniform sampler2D colorTex;

void main(void)
{
	vec2 texcoord = gl_TexCoord[0].st;
	float alpha = ceil(texture2D(colorTex, texcoord).w);
	gl_FragColor = gl_Color * alpha;
}