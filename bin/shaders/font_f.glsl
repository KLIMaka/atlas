uniform sampler2D font;

uniform vec4 fcolor;
uniform vec4 bcolor;
uniform float thickness;

varying vec2 tc;

void main(void) {

	float glyph = pow(texture2D(font, tc), thickness);
	vec4 c = vec4(mix(bcolor.rgb, fcolor.rgb, glyph), bcolor.a);
	
	gl_FragColor = c; 
}