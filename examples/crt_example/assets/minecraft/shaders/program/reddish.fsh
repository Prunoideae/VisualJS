#version 150

// The sampler, note that this is specified in crt.json
uniform sampler2D DiffuseSampler;

// Define the inputs
in vec2 texCoord;
in vec2 oneTexel;

// These are uniforms that we defined in the reddish.json
// How much red to add
uniform float redShift;

// Define the output
out vec4 outColor;

void main()
{
    // Get the color from the texture
    vec4 color = texture(DiffuseSampler, texCoord);

    // Apply the red shift
    color.r += redShift;

    // Output the color
    outColor = color;
}