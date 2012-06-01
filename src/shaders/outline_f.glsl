uniform sampler2D heightTex;

uniform vec3 worldPos;


void main(void)
{
	vec2 texcoord = gl_TexCoord[0].st;
	vec2 tx1 = texcoord * 0.92 + 0.04 ;
	float alpha = floor(texture2D(heightTex, texcoord).w + 0.3);
	float alpha1 = floor(texture2D(heightTex, tx1).w + 0.3);
	
	float outline = (1.0 - alpha) * alpha1;
	
	if (outline == 0.0)
		discard;
	
	gl_FragColor = outline;
}