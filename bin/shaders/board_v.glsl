in vec3 position;

void main(void)
{
    gl_Position = gl_ModelViewProjectionMatrix * vec4(position, 1.0);
    gl_TexCoord[0] = gl_MultiTexCoord0;
}