uniform sampler2D colorTex;
uniform sampler2D heightTex;

in vec4 pos_scale;
in vec4 spriteH;

const float one_1024 = 1.0 / 1024.0;

void main(void)
{
	vec2 texcoord = gl_TexCoord[0].st;
	float height = texture2D(heightTex, texcoord).x * spriteH * pos_scale.w + pos_scale.z;
	float alpha = floor(texture2D(heightTex, texcoord).w + 0.3);
	gl_FragColor = gl_Color;
	gl_FragDepth = 1.0 + one_1024 - (height * one_1024) * alpha;
}