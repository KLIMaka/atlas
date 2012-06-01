uniform sampler2D colorTex;
uniform sampler2D normalTex;
uniform sampler2D heightTex;

in vec4 pos_scale;
in vec4 spriteH;

uniform vec3 lightPos;

const float sqrt2 = 0.70710678118654752440084436210485;

const float lightRadius = 1.0 / 4096.0;
const vec3 ambient = vec3(0.0, 0.0, 0.0);
const vec3 lightColor = vec3(1.0, 1.0, 1.0);
const vec3 lightDir = vec3(0.0, sqrt2, -sqrt2);

const float cos30 = 0.86602540378443864676372317075294;
const float sin30 = 0.5;
const float one_1024 = 1.0 / 1024.0;
const vec3 coordD = vec3(1.0, 2.0, 1.0);

void main(void)
{
	vec2 texcoord = gl_TexCoord[0].st;
	
	vec3 normalWS = texture2D(normalTex, texcoord).xyz;
	float alpha = floor(texture2D(normalTex, texcoord).w + 0.3);
	
	normalWS = normalWS * 2.0 - 1.0;
	normalWS = vec3(normalWS.x, -normalWS.z, normalWS.y); 
	//normalWS = vec3(normalWS.x, normalWS.y*sin30 - normalWS.z*cos30, normalWS.z*sin30 + normalWS.y*cos30);
	normalWS = normalize(normalWS);
	
	float height = texture2D(heightTex, texcoord).x * spriteH.x * pos_scale.w + pos_scale.z;
	vec3 world = gl_FragCoord.xyz - vec3(0.0, height, -height);
	vec3 toLight = (lightPos - world) * coordD;
	
	//float cone = clamp(dot(normalize(toLight), -lightDir), 0.0, 1.0);
	vec4 diffuse = texture2D(colorTex, texcoord);
	float lamb = pow(clamp(dot(normalWS, normalize(toLight)), 0.0, 1.0), 1.0  + diffuse.r * 4.0);
	diffuse.rgb = diffuse.rgb  * gl_Color.rgb + gl_SecondaryColor.rgb;
	float atten = clamp(1.0 - length(toLight) * lightRadius, 0.0, 1.0);
	
	lamb += atten / 4.0;
	
	
	gl_FragColor = vec4(atten * atten * lamb * diffuse.xyz * lightColor  + ambient, alpha);
	gl_FragDepth = 1.0 + one_1024 - (height * one_1024) * alpha;

}