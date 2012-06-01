varying vec4 color;
varying mat2 rot; 
varying vec4 ntc;

void main(void) {

	vec2 tc = (gl_PointCoord - 0.5) * rot * ntc.xy + 0.5;
	
	if (any(lessThan(tc, vec2(0,0))) || any(greaterThan(tc, vec2(1,1)))) {
		discard; 
	}
	
	gl_FragColor = color;
}