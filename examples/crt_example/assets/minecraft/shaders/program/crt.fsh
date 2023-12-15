#version 150

// The sampler, note that this is specified in crt.json
uniform sampler2D DiffuseSampler;

// Define the inputs
in vec2 texCoord;
in vec2 oneTexel;

// These are uniforms that we defined in the crt.json
uniform vec2 InSize;
uniform vec3 Gray;
uniform vec3 RedMatrix;
uniform vec3 GreenMatrix;
uniform vec3 BlueMatrix;
uniform vec3 Offset;
uniform vec3 ColorScale;

// Define the output
// Since we are computing the color here, it's a vector of 4 floats,
// representing r, g, b, and a.
out vec4 fragColor;

void main() {
    // Samples texture from the sampler with the given texCoord
    // so the InTexel is the rgba value of the original texture
    // at given texCoord(xy)
    vec4 InTexel = texture(DiffuseSampler, texCoord);

    // Color Matrix
    // by default rgb value is extracted by multiplying with an
    // one-hot matrix (like [0,1,0] or [1,0,0]), however, in cases
    // you might find sampling a color from other colors useful,
    // like the creeper effect.
    float RedValue = dot(InTexel.rgb, RedMatrix);
    float GreenValue = dot(InTexel.rgb, GreenMatrix);
    float BlueValue = dot(InTexel.rgb, BlueMatrix);
    vec3 OutColor = vec3(RedValue, GreenValue, BlueValue);

    // Saturation
    float Luma = dot(OutColor, Gray);
    // Desaturate by setting Luma as rgb values
    OutColor = vec3(Luma, Luma, Luma);

    // Offset & Scale
    // In the tick.js, Offset is set to [0,0.2,0], so the color will
    // be green-ish, you can modify to other color as well
    OutColor = (OutColor * ColorScale) + Offset;

    // Scanline - borrowed from https://www.shadertoy.com/view/ldXGW4
    // You might find useful shader codes as example/tutorial on this
    // website, but you need to be careful about their licenses, too.
    float scanline = sin(texCoord.y*400.0)*0.04*1.0;
    OutColor -= scanline;

    fragColor = vec4(OutColor, 1.0);
}
